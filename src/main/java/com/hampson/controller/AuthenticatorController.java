package com.hampson.controller;

import static com.google.api.services.calendar.CalendarScopes.CALENDAR;
import static com.hampson.calendar.Configurations.CLIENT_ID;
import static com.hampson.calendar.Configurations.CLIENT_SECRET;
import static com.hampson.calendar.Configurations.REDIRECT_URI;
import static java.util.Collections.singleton;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

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

		return new ModelAndView("redirect:" + authorizationUrl.toURL());
	}

}
