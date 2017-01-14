package com.hampson.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hampson.dao.AppointmentDAO;

@Controller
public class AuthenticationCallbackController {

	@RequestMapping("/oauth2callback")
	public String redirect(HttpServletRequest request, Model model, @RequestParam("code") String authCode)
			throws IOException {

		request.getSession().setAttribute("authCode", authCode);
		
		model.addAttribute("appointments", new AppointmentDAO().getAllAppointments(authCode));

		return "appointmentCalendar";
	}
}
