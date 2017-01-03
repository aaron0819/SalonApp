/*package com.hampson.calendar;

import static com.google.api.services.calendar.CalendarScopes.CALENDAR;
import static com.hampson.calendar.Configurations.CLIENT_ID;
import static com.hampson.calendar.Configurations.CLIENT_SECRET;
import static com.hampson.calendar.Configurations.REDIRECT_URI;
import static java.util.Collections.singleton;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

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

public class GoogleCalendarAuthenticator {

	public static void main(String[] args) throws IOException {
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		Set<String> scope = singleton(CALENDAR);
		String clientId = CLIENT_ID;
		String clientSecret = CLIENT_SECRET;
		String redirectUri = REDIRECT_URI;

		AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
				jsonFactory, clientId, clientSecret, scope);
		AuthorizationCodeFlow codeFlow = codeFlowBuilder.build();

		//Session ID later?
		String userId = "testUser";

		// "redirect" to the authentication url
				AuthorizationCodeRequestUrl authorizationUrl = codeFlow.newAuthorizationUrl();
		authorizationUrl.setRedirectUri(redirectUri);
		System.out.println("Go to the following address:");
		System.out.println(authorizationUrl);

		// Use code to get auth token
		System.out.print("Enter the code returned as a GET parameter: ");
		Scanner sc = new Scanner(System.in);
		String code = sc.nextLine();
		sc.close();
		
		AuthorizationCodeTokenRequest tokenRequest = codeFlow.newTokenRequest(code);
		tokenRequest.setRedirectUri(redirectUri);
		TokenResponse tokenResponse = tokenRequest.execute();

		Credential credential = codeFlow.createAndStoreCredential(tokenResponse, userId);

		HttpRequestInitializer initializer = credential;
		Calendar.Builder serviceBuilder = new Calendar.Builder(httpTransport, jsonFactory, initializer);
		serviceBuilder.setApplicationName(Configurations.APP_NAME);
		Calendar calendar = serviceBuilder.build();

		Calendar.CalendarList.List listRequest = calendar.calendarList().list();
		CalendarList feed = listRequest.execute();
		for (CalendarListEntry entry : feed.getItems()) {
			System.out.println("ID: " + entry.getId());
			System.out.println("Summary: " + entry.getSummary());
		}
	}
}
*/