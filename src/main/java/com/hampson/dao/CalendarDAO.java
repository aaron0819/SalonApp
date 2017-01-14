package com.hampson.dao;

import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.hampson.calendar.Configurations;
import com.hampson.util.AuthenticatorUtil;

public class CalendarDAO {

	public Calendar getCalendar(String authCode) throws IOException {
		AuthenticatorUtil authenticatorUtil = new AuthenticatorUtil();
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		Credential credential = authenticatorUtil.retrieveCredentials(authCode, httpTransport, jsonFactory);

		HttpRequestInitializer initializer = credential;
		Calendar calendar = new Calendar.Builder(httpTransport, jsonFactory, initializer)
				.setApplicationName(Configurations.APP_NAME).build();
		
		return calendar;
	}

}
