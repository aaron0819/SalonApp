package com.hampson.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

@Controller
public class AddAppointmentController {

	@RequestMapping("/addAppointment")
	public String addEvent(HttpServletRequest request,
			@RequestParam("appointmentType") String appointmentType,
			@RequestParam("customerFirstName") String customerFirstName,
			@RequestParam("customerLastName") String customerLastName,
			@RequestParam("customerPhoneNumber") String customerPhoneNumber,
			@RequestParam("appointmentDate") String appointmentDate,
			@RequestParam("appointmentStartTime") String appointmentStartTime,
			@RequestParam("appointmentEndTime") String appointmentEndTime) throws IOException {
		
		Event event = new Event().setSummary(appointmentType).setLocation("800 Howard St., San Francisco, CA 94103")
				.setDescription("Customer:" + customerFirstName + " " + customerLastName + ";\nCustomer Phone Number:"
						+ customerPhoneNumber + ";");
		
		DateTime startDateTime = new DateTime(appointmentDate + "T" + appointmentStartTime + ":00-05:00");
		EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("America/New_York");
		event.setStart(start);

		DateTime endDateTime = new DateTime(appointmentDate + "T" + appointmentEndTime + ":00-05:00");
		EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("America/New_York");
		event.setEnd(end);

		String calendarId = "primary";
		Calendar calendar = (Calendar) request.getSession().getAttribute("calendar");
		event = calendar.events().insert(calendarId, event).execute();
				
		return "successfulAppointmentAdd";
	}
}
