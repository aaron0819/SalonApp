package com.hampson.salonapp.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.hampson.salonapp.iface.AppointmentDao;
import com.hampson.salonapp.model.Appointment;

@Repository
public class AppointmentDaoPostgresImpl extends JdbcDaoSupport implements AppointmentDao {

	@Autowired
	DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Override
	public void insert(Appointment appointment) {
		String sql = "INSERT INTO Appointments (id, service, time_from, time_to, stylist, customer) VALUES (?, ?, ?, ?, ?, ?)";
		getJdbcTemplate().update(sql, new Object[] { 0, appointment.getAppointmentDesc(), appointment.getStartTime(),
				appointment.getEndTime(), appointment.getStylist(), appointment.getCustomer() });
	}

	@Override
	public List<Appointment> retrieveAllAppointments() {
		String sql = "SELECT * FROM Appointments";
		List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

		List<Appointment> result = new ArrayList<Appointment>();
		for (Map<String, Object> row : rows) {
			Appointment appt = new Appointment();
			
			appt.setAppointmentDesc((String) row.get("service"));
			appt.setStartTime(((Date) row.get("start_time")).toString());
			appt.setEndTime(((Date) row.get("end_time")).toString());
			appt.setStylist(null);
			appt.setCustomer(null);
			
			result.add(appt);
		}
		return result;
	}

	@Override
	public Appointment findAppointmentByStylistId(long stylistId) {
		// TODO Auto-generated method stub
		return null;
	}
}
