package com.nomina.companysearchservice.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomina.companysearchservice.datascrapper.DataModel;
import com.nomina.companysearchservice.dto.SimilarNameDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class WipoRepository {

    private final JdbcTemplate jdbcTemplate;

    public WipoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SimilarNameDto> findSimilarNames(String query) {
        String sql = "SELECT id, mark_name, similarity(mark_name, ?) AS similarity_score, link " +
                "FROM wipo " +
                "ORDER BY similarity_score DESC " +
                "LIMIT 20";

        return jdbcTemplate.query(sql, new Object[]{query}, new SimilarNameRowMapper());
    }

    public SimilarNameDto findById(int id) {
        String sql = "SELECT id, mark_name, 0.0 AS similarity_score, link " +
                "FROM wipo " +
                "WHERE id = ?";

        List<SimilarNameDto> results = jdbcTemplate.query(sql, new Object[]{id}, new SimilarNameRowMapper());
        return results.isEmpty() ? null : results.get(0);
    }

    public void updateCompanyData(int id, DataModel dataModel) throws JsonProcessingException {
        String sql = "UPDATE wipo SET mark_name = ?, data = ?::jsonb WHERE id = ?";

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(dataModel.getData());

        jdbcTemplate.update(sql, dataModel.getMarkName(), jsonData, id);
    }

    private static class SimilarNameRowMapper implements RowMapper<SimilarNameDto> {
        @Override
        public SimilarNameDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SimilarNameDto(
                    rs.getInt("id"),
                    rs.getString("mark_name"),
                    rs.getDouble("similarity_score"),
                    rs.getString("link")
            );
        }
    }
}
