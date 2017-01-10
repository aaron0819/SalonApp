package com.hampson.controller;

import static com.google.api.services.calendar.CalendarScopes.CALENDAR;
import static com.hampson.calendar.Configurations.CLIENT_ID;
import static com.hampson.calendar.Configurations.CLIENT_SECRET;
import static com.hampson.calendar.Configurations.REDIRECT_URI;
import static java.util.Collections.singleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
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

@Controller
public class AuthenticatorController {

	private Calendar calendar;

	@RequestMapping("/authenticate")
	public ModelAndView authenticate(Model model) {
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		Set<String> scope = singleton(CALENDAR);
		String clientId = CLIENT_ID;
		String clientSecret = CLIENT_SECRET;
		String redirectUri = REDIRECT_URI;

		AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
				jsonFactory, clientId, clientSecret, scope);
		AuthorizationCodeFlow codeFlow = codeFlowBuilder.build();

		// "redirect" to the authentication url
		AuthorizationCodeRequestUrl authorizationUrl = codeFlow.newAuthorizationUrl();
		authorizationUrl.setRedirectUri(redirectUri);

		return new ModelAndView("redirect:" + authorizationUrl.toURL());
	}

	@RequestMapping("/oauth2callback")
	public String redirect(HttpServletRequest request, Model model, @RequestParam("code") String authCode)
			throws IOException {

		String userId = "testUser";
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		Set<String> scope = singleton(CALENDAR);
		String clientId = CLIENT_ID;
		String clientSecret = CLIENT_SECRET;
		String redirectUri = REDIRECT_URI;

		if (null == request.getSession().getAttribute("calendar")) {
			AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
					jsonFactory, clientId, clientSecret, scope).setAccessType("offline").setApprovalPrompt("force");

			AuthorizationCodeFlow codeFlow = codeFlowBuilder.build();
			AuthorizationCodeTokenRequest tokenRequest = codeFlow.newTokenRequest(authCode);
			tokenRequest.setRedirectUri(redirectUri);
			TokenResponse tokenResponse = tokenRequest.execute();

			Credential credential = codeFlow.createAndStoreCredential(tokenResponse, userId);

			request.getSession().setAttribute("oauthCredential", credential);

			HttpRequestInitializer initializer = credential;
			setCalendar(new Calendar.Builder(httpTransport, jsonFactory, initializer)
					.setApplicationName(Configurations.APP_NAME).build());

			request.getSession().setAttribute("calendar", calendar);
			request.getSession().setAttribute("code", authCode);
		} else {
			setCalendar((Calendar) request.getSession().getAttribute("calendar"));
		}

		Calendar.CalendarList.List listRequest = getCalendar().calendarList().list();
		CalendarList feed = listRequest.execute();
		List<Appointment> appointments = new ArrayList<Appointment>();

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
				Events events = getCalendar().events().list(entry.getId()).setPageToken(pageToken).execute();
				List<Event> items = events.getItems();
				for (Event event : items) {
					Event e = getCalendar().events().get(entry.getId(), event.getId()).execute();

					date = parseDate(e.getStart().toString());
					startTime = parseStartTime(e.getStart().toString());
					endTime = parseEndTime(e.getEnd().toString());
					customerName = parseCustomerName(e.getDescription());
					customerPhoneNumber = parsePhoneNumber(e.getDescription());

					appointments.add(new Appointment(e.getSummary(), date, startTime, endTime,
							new Customer(customerName[0], customerName[1], customerPhoneNumber)));
				}
				pageToken = events.getNextPageToken();
			} while (pageToken != null);

		}

		model.addAttribute("calendarEntries", feed);
		model.addAttribute("appointments", appointments);

		return "authenticated";
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

			System.out.println(description);
			
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

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
}
