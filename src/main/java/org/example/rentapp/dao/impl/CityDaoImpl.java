package org.example.rentapp.dao.impl;

import org.example.rentapp.dao.interfaces.CityDao;
import org.example.rentapp.entities.City;
import org.springframework.stereotype.Repository;

@Repository
public class CityDaoImpl extends AbstractDaoImpl<City, Long> implements CityDao {


}
