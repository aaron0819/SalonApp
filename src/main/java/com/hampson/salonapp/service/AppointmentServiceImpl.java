package com.hampson.salonapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hampson.salonapp.iface.AppointmentDao;
import com.hampson.salonapp.iface.AppointmentService;
import com.hampson.salonapp.model.Appointment;

public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	AppointmentDao appointmentDao;

	@Override
	public void insert(Appointment appointment) {
		appointmentDao.insert(appointment);
	}

	@Override
	public List<Appointment> retrieveAllAppointments() {
		return appointmentDao.retrieveAllAppointments();
	}

	@Override
	public Appointment findAppointmentByStylistId(long stylistId) {
		return null;
	}
}
