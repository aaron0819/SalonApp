package com.hampson.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.api.services.calendar.Calendar;
import com.hampson.dao.AppointmentDAO;
import com.hampson.dao.CalendarDAO;

@Controller
public class AuthenticationCallbackController {

	@RequestMapping("/oauth2callback")
	public String redirect(HttpServletRequest request, Model model, @RequestParam("code") Optional<String> authCode)
			throws IOException {
		try {
			Calendar calendar;
			if (null == request.getSession().getAttribute("calendar")) {
				calendar = new CalendarDAO().getCalendar(authCode.get());
				request.getSession().setAttribute("calendar", calendar);
			} else {
				calendar = (Calendar) request.getSession().getAttribute("calendar");
			}
			
			model.addAttribute("appointments", new AppointmentDAO().getAllAppointments(calendar));
		} catch(Exception e) {
			model.addAttribute("error", "There was an error during the retrieval of your calendar");
		}
		return "appointmentCalendar";
	}
}
