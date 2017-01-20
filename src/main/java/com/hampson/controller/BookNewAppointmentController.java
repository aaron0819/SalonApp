package com.hampson.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BookNewAppointmentController {

	@RequestMapping("/bookNewAppointment")
	public String bookNewAppointment(Model model) {

		return "bookNewAppointment";
	}
}
