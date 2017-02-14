package com.hampson.util;

import java.util.Comparator;
import java.util.List;

import com.hampson.model.Appointment;

public class SortingUtil {

	public List<Appointment> sortAppointments(List<Appointment> appointments) {
		appointments.sort(Comparator.comparing(Appointment::getDate).thenComparing(Appointment::getStartTime));
		
		return appointments;
	}

}
