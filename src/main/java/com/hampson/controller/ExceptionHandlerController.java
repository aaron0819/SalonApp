package com.hampson.controller;

import static java.time.LocalDateTime.now;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlerController {

	public static final String DEFAULT_ERROR_VIEW = "error";

	@ExceptionHandler(value = { Exception.class, RuntimeException.class })
	public ModelAndView defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
		ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);

		mav.addObject("datetime", now());
		mav.addObject("exception", e.getMessage() + "\n\n" + e.getStackTrace() + "\n\nSTATUS CODE: " + response.getStatus());
		mav.addObject("url", request.getRequestURL());

		System.out.println("ERROR: " + now() + "\n" + e.getMessage() + "\n\n" + e.getStackTrace());
		
		return mav;
	}
}