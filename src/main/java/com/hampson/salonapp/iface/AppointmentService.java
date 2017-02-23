package com.hampson.salonapp.iface;

import java.util.List;

import com.hampson.salonapp.model.Appointment;

public interface AppointmentService {

	void insert(Appointment appointment);

	List<Appointment> retrieveAllAppointments();

	Appointment findAppointmentByStylistId(long stylistId);
}
