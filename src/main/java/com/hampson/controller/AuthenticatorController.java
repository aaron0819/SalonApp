package com.hampson.controller;

import static com.google.api.services.calendar.CalendarScopes.CALENDAR;
import static com.hampson.calendar.Configurations.CLIENT_ID;
import static com.hampson.calendar.Configurations.CLIENT_SECRET;
import static com.hampson.calendar.Configurations.REDIRECT_URI;
import static java.util.Collections.singleton;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
		System.out.println(authorizationUrl);
		return new ModelAndView("redirect:" + authorizationUrl.toURL());
	}

	@RequestMapping("/oauth2callback")
	public String redirect(Model model, @RequestParam("code") String authCode) throws IOException {
		String userId = "testUser";
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		Set<String> scope = singleton(CALENDAR);
		String clientId = CLIENT_ID;
		String clientSecret = CLIENT_SECRET;
		String redirectUri = REDIRECT_URI;

		AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
				jsonFactory, clientId, clientSecret, scope);

		AuthorizationCodeFlow codeFlow = codeFlowBuilder.build();
		AuthorizationCodeTokenRequest tokenRequest = codeFlow.newTokenRequest(authCode);
		tokenRequest.setRedirectUri(redirectUri);
		TokenResponse tokenResponse = tokenRequest.execute();

		Credential credential = codeFlow.createAndStoreCredential(tokenResponse, userId);

		HttpRequestInitializer initializer = credential;
		Calendar calendar = new Calendar.Builder(httpTransport, jsonFactory, initializer)
				.setApplicationName(Configurations.APP_NAME).build();

		Calendar.CalendarList.List listRequest = calendar.calendarList().list();
		CalendarList feed = listRequest.execute();
		List<Appointment> appointments = new ArrayList<Appointment>();

		for (CalendarListEntry entry : feed.getItems()) {
			// if ("Salon Appointments".equalsIgnoreCase(entry.getSummary())) {
			
			//Stops holidays or contact calendars from being pulled in
			if("Contacts".equalsIgnoreCase(entry.getSummary()) || "Holidays in United States".equalsIgnoreCase(entry.getSummary())) {
				continue;
			}
			
			String pageToken = null;
			String date = null;
			String startTime = null;
			String endTime = null;
			
			do {
				Events events = calendar.events().list(entry.getId()).setPageToken(pageToken).execute();
				List<Event> items = events.getItems();
				for (Event event : items) {
					Event e = calendar.events().get(entry.getId(), event.getId()).execute();
					
					date = parseDate(e.getStart().toString());
					startTime = parseStartTime(e.getStart().toString());
					endTime = parseEndTime(e.getEnd().toString());
					
					appointments.add(new Appointment(e.getSummary(), date, startTime, endTime, new Customer("Jane", "Doe", "000-000-0000")));
				}
				pageToken = events.getNextPageToken();
			} while (pageToken != null);
			// }
		}

		model.addAttribute("appointments", appointments);

		return "authenticated";
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
		int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
		int day = Integer.parseInt(date.substring(date.lastIndexOf("-") + 1));
		
		return String.format("%02d %02d %4d", Integer.toString(day) + " " + Integer.toString(month) + " " + Integer.toString(year));
	}
}
