package com.nomina.companysearchservice.repository;

import com.nomina.companysearchservice.dto.CompanyData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CompanyRepository {

    private final JdbcTemplate jdbcTemplate;

    public CompanyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CompanyData> getAllAipaCompaniesWithHashes() {
        String sql = "SELECT id, mark_name, image_path, perceptive_hash, difference_hash, link " +
                "FROM aipa " +
                "WHERE perceptive_hash IS NOT NULL AND difference_hash IS NOT NULL";

        return jdbcTemplate.query(sql, new CompanyDataRowMapper());
    }

    public List<CompanyData> getAllWipoCompaniesWithHashes() {
        String sql = "SELECT id, mark_name, image_path, perceptive_hash, difference_hash, link " +
                "FROM wipo " +
                "WHERE perceptive_hash IS NOT NULL AND difference_hash IS NOT NULL";

        return jdbcTemplate.query(sql, new CompanyDataRowMapper());
    }

    // Row Mapper for CompanyData
    private static class CompanyDataRowMapper implements RowMapper<CompanyData> {
        @Override
        public CompanyData mapRow(ResultSet rs, int rowNum) throws SQLException {
            // Convert numeric from database to BigInteger
            BigInteger perceptiveHash = null;
            BigInteger differenceHash = null;

            Object perceptiveHashObj = rs.getObject("perceptive_hash");
            if (perceptiveHashObj != null) {
                perceptiveHash = new BigInteger(perceptiveHashObj.toString());
            }

            Object differenceHashObj = rs.getObject("difference_hash");
            if (differenceHashObj != null) {
                differenceHash = new BigInteger(differenceHashObj.toString());
            }

            return new CompanyData(
                    rs.getLong("id"),
                    rs.getString("mark_name"),
                    rs.getString("image_path"),
                    perceptiveHash,
                    differenceHash,
                    rs.getString("link")
            );
        }
    }
}
