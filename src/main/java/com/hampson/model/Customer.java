package com.hampson.model;

public class Customer {
	private String firstName;
	private String lastName;
	private String phoneNumber;

	public Customer() {
		super();
	}

	public Customer(String firstName, String lastName, String phoneNumber) {
		super();
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setPhoneNumber(phoneNumber);
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
