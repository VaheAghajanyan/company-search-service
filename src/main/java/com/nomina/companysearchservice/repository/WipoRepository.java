package com.nomina.companysearchservice.repository;

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
        String sql = "SELECT mark_name, similarity(mark_name, ?) AS similarity_score " +
                "FROM wipo " +
                "ORDER BY similarity_score DESC " +
                "LIMIT 10";

        return jdbcTemplate.query(sql, new Object[]{query}, new SimilarNameRowMapper());
    }

    private static class SimilarNameRowMapper implements RowMapper<SimilarNameDto> {
        @Override
        public SimilarNameDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SimilarNameDto(
                    rs.getString("mark_name"),
                    rs.getDouble("similarity_score")
            );
        }
    }
}
