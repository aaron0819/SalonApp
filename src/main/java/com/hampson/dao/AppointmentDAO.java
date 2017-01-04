package com.hampson.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

public class AppointmentDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> getAllAppointments() {
		List<Map<String, Object>> appointments = new ArrayList<Map<String, Object>>();

		String sql = "select appointment.id, appointment.stylist_id, appointment.time, customer.first_name, customer.last_name, customer.phone_num from appointment, customer where (appointment.customer_id = customer.id)";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> row : list) {
			appointments.add(row);
		}

		return appointments;
	}
}
