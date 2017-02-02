package com.hampson.controller;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

public class ExceptionHandlerControllerTest {

	private ExceptionHandlerController controller;
	private HttpServletRequest request;
	private Exception exception;
	private String url;

	@Before
	public void setup() {
		controller = new ExceptionHandlerController();
		request = Mockito.mock(HttpServletRequest.class);
		exception = new Exception("This is a test exception", new Throwable("Throwable message"));
		url = "salonapp-springboot.herokuapp.com";
	}

	@After
	public void tearDown() {
		controller = null;
		request = null;
		exception = null;
		url = null;
	}

	@Test
	public void testDefaultErrorHandler() {
		LocalDateTime expectedDateTime = LocalDateTime.now();
		Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer(url));		
		
		ModelAndView actual = controller.defaultErrorHandler(request, null, exception);

		LocalDateTime actualDateTime = (LocalDateTime) actual.getModel().get("datetime");
		String actualException = (String) actual.getModel().get("exception");
		StringBuffer actualURL = (StringBuffer) actual.getModel().get("url");
		
		assertEquals(DAYS.between(expectedDateTime.toLocalDate(), actualDateTime.toLocalDate()), 0);
		assertEquals(MINUTES.between(expectedDateTime.toLocalTime(), actualDateTime.toLocalTime()), 0);
		assertTrue(actualException.contains(exception.getMessage()));
		assertEquals(url, actualURL.toString());
	}
}