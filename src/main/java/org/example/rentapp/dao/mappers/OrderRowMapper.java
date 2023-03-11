package org.example.rentapp.dao.mappers;

import org.example.rentapp.entities.Order;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderRowMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setCreationDate(rs.getDate("creation_date").toLocalDate());
        order.setPlannedDate(rs.getDate("planned_date").toLocalDate());
        return order;
    }
}
