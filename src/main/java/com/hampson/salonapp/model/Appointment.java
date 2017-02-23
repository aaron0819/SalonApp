package com.hampson.salonapp.model;

import java.io.Serializable;

public class Appointment implements Serializable {

	private static final long serialVersionUID = -3277572530717692763L;

	private String appointmentDesc;
	private String date;
	private String startTime;
	private String endTime;
	private Customer customer;
	private Stylist stylist;

	public Appointment() {
		super();
	}
	
	public Appointment(String appointmentDesc, String date, String startTime, String endTime, Customer customer) {
		super();
		this.setAppointmentDesc(appointmentDesc);
		this.setDate(date);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setCustomer(customer);
	}
	
	public Appointment(String appointmentDesc, String date, String startTime, String endTime, Customer customer, Stylist stylist) {
		this(appointmentDesc, date, startTime, endTime, customer);
		this.setStylist(stylist);
	}

	public String getAppointmentDesc() {
		return appointmentDesc;
	}

	public void setAppointmentDesc(String appointmentDesc) {
		this.appointmentDesc = appointmentDesc;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Stylist getStylist() {
		return stylist;
	}

	public void setStylist(Stylist stylist) {
		this.stylist = stylist;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
