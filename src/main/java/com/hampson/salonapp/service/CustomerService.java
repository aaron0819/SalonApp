package com.hampson.salonapp.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.hampson.model.Customer;

@Service
public class CustomerService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int addCustomer(Customer customer) {
		String sql = "INSERT INTO Customers(id, first_name, last_name, phone_number) VALUES(?,?,?,?)";
		return jdbcTemplate.update(sql, null, customer.getFirstName(), customer.getLastName(),
				customer.getPhoneNumber());
	}

	public List<Customer> getAllCustomers() {
		return jdbcTemplate.query("SELECT * FROM Customers", new RowMapper<Customer>() {

			public Customer mapRow(ResultSet rs, int arg1) throws SQLException {
				Customer c = new Customer();

				c.setId(rs.getInt("id"));
				c.setFirstName(rs.getString("first_name"));
				c.setLastName(rs.getString("last_name"));
				c.setPhoneNumber(rs.getString("phone_number"));

				return c;
			}

		});
	}
}