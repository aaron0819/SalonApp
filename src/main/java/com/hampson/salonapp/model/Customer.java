package com.hampson.salonapp.model;

import java.io.Serializable;

public class Customer implements Serializable {

	private static final long serialVersionUID = 2822027024447584056L;

	private long id;
	private String firstName;
	private String lastName;
	private String phoneNumber;

	protected Customer() {
		super();
	}

	public Customer(String firstName, String lastName, String phoneNumber) {
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setPhoneNumber(phoneNumber);
	}

	@Override
	public String toString() {
		return String.format("Customer[id=%d, firstName='%s', lastName='%s', phoneNumber='%s']", id, firstName,
				lastName, phoneNumber);
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
