package com.hampson.salonapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hampson.salonapp.iface.AppointmentService;
import com.hampson.salonapp.model.Appointment;

@Controller
public class AppointmentCalendarController {
	
	@Autowired
	private AppointmentService appointmentService;
	
	@RequestMapping("/test")
	public String getAllAppointments(Model model) {
		
		List<Appointment> appointments = appointmentService.retrieveAllAppointments();
		
		model.addAttribute("appointments", appointments);
		
		return "index";
	}
}
