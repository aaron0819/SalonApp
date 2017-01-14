package com.hampson.util;

import static com.google.api.services.calendar.CalendarScopes.CALENDAR;
import static com.hampson.calendar.Configurations.CLIENT_ID;
import static com.hampson.calendar.Configurations.CLIENT_SECRET;
import static com.hampson.calendar.Configurations.REDIRECT_URI;
import static java.util.Collections.singleton;

import java.io.IOException;
import java.util.Set;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class AuthenticatorUtil {

	public AuthenticatorUtil() {
		super();
	}
	
	public Credential retrieveCredentials(String authCode, HttpTransport httpTransport, JsonFactory jsonFactory) throws IOException {

		String userId = "testUser";

		Set<String> scope = singleton(CALENDAR);
		String clientId = CLIENT_ID;
		String clientSecret = CLIENT_SECRET;
		String redirectUri = REDIRECT_URI;

		AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
				jsonFactory, clientId, clientSecret, scope).setAccessType("offline").setApprovalPrompt("force");

		AuthorizationCodeFlow codeFlow = codeFlowBuilder.build();
		AuthorizationCodeTokenRequest tokenRequest = codeFlow.newTokenRequest(authCode);
		tokenRequest.setRedirectUri(redirectUri);
		TokenResponse tokenResponse = tokenRequest.execute();

		return codeFlow.createAndStoreCredential(tokenResponse, userId);
	}

}
