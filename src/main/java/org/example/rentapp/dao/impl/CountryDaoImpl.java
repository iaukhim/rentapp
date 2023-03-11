package org.example.rentapp.dao.impl;

import org.example.rentapp.dao.interfaces.CountryDao;
import org.example.rentapp.entities.Country;
import org.springframework.stereotype.Repository;

@Repository
public class CountryDaoImpl extends AbstractDaoImpl<Country, Long> implements CountryDao {


}
