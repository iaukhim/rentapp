package org.example.rentapp.dao.mappers;

import org.example.rentapp.entities.Facility;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FacilityRowMapper implements RowMapper<Facility> {

    @Override
    public Facility mapRow(ResultSet rs, int rowNum) throws SQLException {
        Facility facility = new Facility();
        facility.setId(rs.getLong("id"));
        facility.setName(rs.getString("name"));
        facility.setSpace(rs.getDouble("space"));
        return facility;
    }
}
