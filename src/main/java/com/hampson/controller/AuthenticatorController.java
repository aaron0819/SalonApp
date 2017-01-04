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
		List<String> appointments = new ArrayList<String>();

		for (CalendarListEntry entry : feed.getItems()) {
			String pageToken = null;
			do {
				Events events = calendar.events().list(entry.getId()).setPageToken(pageToken).execute();
				List<Event> items = events.getItems();
				for (Event event : items) {
					Event e = calendar.events().get(entry.getId(), event.getId()).execute();
					appointments.add(
							e.getSummary() + " " + e.getStart().toPrettyString() + " " + e.getEnd().toPrettyString());
				}
				pageToken = events.getNextPageToken();
			} while (pageToken != null);

		}

		model.addAttribute("authCode", authCode);
		model.addAttribute("calendarEntries", feed.getItems());
		model.addAttribute("appointments", appointments);

		return "authenticated";
	}
}
