package com.hampson.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.hampson.model.Appointment;
import com.hampson.model.Customer;
import com.hampson.util.SortingUtil;

public class AppointmentDAO {

	private List<Appointment> appointments;

	public AppointmentDAO() {
		setAppointments(new ArrayList<Appointment>());
	}

	public List<Appointment> getAllAppointments(Calendar calendar) throws IOException {
		Calendar.CalendarList.List listRequest = calendar.calendarList().list();
		CalendarList feed = listRequest.execute();

		for (CalendarListEntry entry : feed.getItems()) {
			// Stops holidays or contact calendars from being pulled in
			if ("Contacts".equalsIgnoreCase(entry.getSummary())
					|| "Holidays in United States".equalsIgnoreCase(entry.getSummary())) {
				continue;
			}

			String pageToken = null;
			String date = null;
			String startTime = null;
			String endTime = null;
			String customerName[] = null;
			String customerPhoneNumber = null;

			do {
				Events events = calendar.events().list(entry.getId()).setPageToken(pageToken).execute();
				List<Event> items = events.getItems();
				for (Event event : items) {
					Event e = calendar.events().get(entry.getId(), event.getId()).execute();

					if (null != e.getDescription()) {

						date = parseDate(e.getStart().toString());
						startTime = parseStartTime(e.getStart().toString());
						endTime = parseEndTime(e.getEnd().toString());
						customerName = parseCustomerName(e.getDescription());
						customerPhoneNumber = parsePhoneNumber(e.getDescription());

						getAppointments().add(new Appointment(e.getSummary(), date, startTime, endTime,
								new Customer(customerName[0], customerName[1], customerPhoneNumber)));
					}
				}
				pageToken = events.getNextPageToken();
			} while (pageToken != null);

		}

		return new SortingUtil().sortAppointments(appointments);

/*		Map<String, List<Appointment>> separatedAppointments = new HashMap<String, List<Appointment>>();

		for (Appointment a : appointments) {
			if (separatedAppointments.containsKey(a.getDate())) {
				separatedAppointments.get(a.getDate()).add(a);
			} else {
				separatedAppointments.put(a.getDate(), new ArrayList<>());
				separatedAppointments.get(a.getDate()).add(a);
			}
		}

		return separatedAppointments;*/

	}

	private String[] parseCustomerName(String description) {
		String[] customerName = new String[2];

		if (null != description && description.contains("Customer:")) {
			customerName = description.substring(9, description.indexOf(";")).split(" ");
		}

		if (null == customerName[0]) {
			customerName[0] = "N/A";
			customerName[1] = "N/A";
		}

		return customerName;
	}

	private String parsePhoneNumber(String description) {
		String customerPhoneNumber = "N/A";

		if (null != description && description.contains("Customer Phone Number:")) {

			description = description.substring(description.indexOf("Customer Phone Number:"),
					description.lastIndexOf(";"));

			customerPhoneNumber = description.substring(22, 32);
		}
		return customerPhoneNumber;
	}

	private String parseEndTime(String end) {
		try {
			end = end.substring(end.lastIndexOf("T") + 1, end.indexOf(".") - 3);
		} catch (StringIndexOutOfBoundsException e) {
			end = "N/A";
		}
		return end;
	}

	private String parseStartTime(String start) {

		try {
			start = start.substring(start.lastIndexOf("T") + 1, start.indexOf(".") - 3);
		} catch (StringIndexOutOfBoundsException e) {
			start = "N/A";
		}
		return start;
	}

	private String parseDate(String date) {
		try {
			date = date.substring(date.indexOf(":") + 2, date.lastIndexOf("T"));
			int year = Integer.parseInt(date.substring(0, date.indexOf("-")));
			int day = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
			int month = Integer.parseInt(date.substring(date.lastIndexOf("-") + 1));

			date = String.format("%02d-%02d-%4d", day, month, year);
		} catch (StringIndexOutOfBoundsException e) {
			date = "N/A";
		}

		return date;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

}
