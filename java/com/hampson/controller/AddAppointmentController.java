package com.hampson.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

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
	public String addEvent(HttpServletRequest request, Model model,
			@RequestParam("appointmentType") String appointmentType,
			@RequestParam("customerFirstName") String customerFirstName,
			@RequestParam("customerLastName") String customerLastName,
			@RequestParam("customerPhoneNumber") String customerPhoneNumber,
			@RequestParam("appointmentDate") String appointmentDate,
			@RequestParam("appointmentStartTime") String appointmentStartTime,
			@RequestParam("appointmentEndTime") String appointmentEndTime) throws IOException {
		
		System.out.println(request.getQueryString());
		
		Event event = new Event().setSummary(appointmentType).setLocation("800 Howard St., San Francisco, CA 94103")
				.setDescription("Customer:" + customerFirstName + " " + customerLastName + ";\nCustomer Phone Number:"
						+ customerPhoneNumber + ";");
		
		DateTime startDateTime = new DateTime(appointmentDate + "T" + appointmentStartTime + ":00-05:00");
		EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("America/New_York");
		event.setStart(start);

		DateTime endDateTime = new DateTime(appointmentDate + "T" + appointmentEndTime + ":00-05:00");
		EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("America/New_York");
		event.setEnd(end);

		// EventAttendee[] attendees = new EventAttendee[] { new
		// EventAttendee().setEmail("lpage@example.com"),
		// new EventAttendee().setEmail("sbrin@example.com"), };
		// event.setAttendees(Arrays.asList(attendees));

		// EventReminder[] reminderOverrides = new EventReminder[] {
		// new EventReminder().setMethod("email").setMinutes(24 * 60),
		// new EventReminder().setMethod("popup").setMinutes(10),
		// };
		// Event.Reminders reminders = new Event.Reminders()
		// .setUseDefault(false)
		// .setOverrides(Arrays.asList(reminderOverrides));
		// event.setReminders(reminders);

		String calendarId = "primary";
		Calendar calendar = (Calendar) request.getSession().getAttribute("calendar");
		event = calendar.events().insert(calendarId, event).execute();
		System.out.printf("Event created: %s\n", event.getHtmlLink());

		return "redirect:/oauth2callback?code=" + request.getSession().getAttribute("code");
	}
}
