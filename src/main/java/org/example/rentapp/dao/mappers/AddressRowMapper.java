package org.example.rentapp.dao.mappers;

import org.example.rentapp.entities.Address;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AddressRowMapper implements RowMapper<Address> {
    @Override
    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address address = new Address();

        address.setId(rs.getLong("id"));
        address.setDistrict(rs.getString("district"));
        address.setStreetName(rs.getString("street_name"));
        address.setStreetNumber(rs.getString("street_number"));
        return address;
    }
}
