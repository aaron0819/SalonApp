package com.hampson.dao;

import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

	public List<String> getAppointments() {
		List<String> appointments = new ArrayList<String>();
		
		appointments.add("Test");
		appointments.add("Test2");
		
		return appointments;
	}

}
