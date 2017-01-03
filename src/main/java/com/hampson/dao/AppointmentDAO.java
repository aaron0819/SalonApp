package com.hampson.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class AppointmentDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> getAllAppointments() {
		List<Map<String, Object>> appointments = new ArrayList<Map<String, Object>>();

		String sql = "SELECT * from appointment";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> row : list) {
			appointments.add(row);
		}

		return appointments;
	}

}
