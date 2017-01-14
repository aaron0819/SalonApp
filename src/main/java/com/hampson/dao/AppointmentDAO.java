package com.hampson.dao;

import static com.google.api.services.calendar.CalendarScopes.CALENDAR;
import static com.hampson.calendar.Configurations.CLIENT_ID;
import static com.hampson.calendar.Configurations.CLIENT_SECRET;
import static com.hampson.calendar.Configurations.REDIRECT_URI;
import static java.util.Collections.singleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.hampson.calendar.Configurations;
import com.hampson.model.Appointment;
import com.hampson.model.Customer;
import com.hampson.util.AuthenticatorUtil;

public class AppointmentDAO {

	private List<Appointment> appointments;
	
	public AppointmentDAO() {
		setAppointments(new ArrayList<Appointment>());
	}
	
	public List<Appointment> getAllAppointments(String authCode) throws IOException {
		AuthenticatorUtil authenticatorUtil = new AuthenticatorUtil();
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		
		Credential credential = authenticatorUtil.retrieveCredentials(authCode, httpTransport, jsonFactory);

		HttpRequestInitializer initializer = credential;
		Calendar calendar = new Calendar.Builder(httpTransport, jsonFactory, initializer)
				.setApplicationName(Configurations.APP_NAME).build();

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

					date = parseDate(e.getStart().toString());
					startTime = parseStartTime(e.getStart().toString());
					endTime = parseEndTime(e.getEnd().toString());
					customerName = parseCustomerName(e.getDescription());
					customerPhoneNumber = parsePhoneNumber(e.getDescription());

					getAppointments().add(new Appointment(e.getSummary(), date, startTime, endTime,
							new Customer(customerName[0], customerName[1], customerPhoneNumber)));
				}
				pageToken = events.getNextPageToken();
			} while (pageToken != null);

		}
		return appointments;
	}

	private String[] parseCustomerName(String description) {
		String[] customerName = new String[2];

		if (null != description && description.contains("Customer:")) {
			customerName = description.substring(9, description.indexOf(";")).split(" ");
		}

		return customerName;
	}

	private String parsePhoneNumber(String description) {
		String customerPhoneNumber = "";

		if (null != description && description.contains("Customer Phone Number:")) {

			description = description.substring(description.indexOf("Customer Phone Number:"),
					description.lastIndexOf(";"));

			customerPhoneNumber = description.substring(22, 32);
		}
		return customerPhoneNumber;
	}

	private String parseEndTime(String end) {
		return end.substring(end.lastIndexOf("T") + 1, end.indexOf(".") - 3);
	}

	private String parseStartTime(String start) {
		return start.substring(start.lastIndexOf("T") + 1, start.indexOf(".") - 3);
	}

	private String parseDate(String date) {
		date = date.substring(date.indexOf(":") + 2, date.lastIndexOf("T"));
		int year = Integer.parseInt(date.substring(0, date.indexOf("-")));
		int day = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
		int month = Integer.parseInt(date.substring(date.lastIndexOf("-") + 1));

		return String.format("%02d-%02d-%4d", day, month, year);
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

}
