package org.example.rentapp.dao.mappers;

import org.example.rentapp.dao.interfaces.CountryDao;
import org.example.rentapp.entities.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CityRowMapper implements RowMapper<City> {

    private final CountryDao countryDao;

    @Autowired
    public CityRowMapper(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    @Override
    public City mapRow(ResultSet rs, int rowNum) throws SQLException {
        City city = new City();
        city.setId(rs.getLong("id"));
        city.setName(rs.getString("name"));
        city.setCountry(countryDao.loadById(rs.getLong("country_id")).get());
        return city;
    }

}
