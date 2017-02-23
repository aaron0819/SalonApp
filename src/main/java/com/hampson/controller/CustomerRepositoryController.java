package com.hampson.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hampson.model.Customer;

public class CustomerRepositoryController {

	@RequestMapping("/allcustomers")
	public String users(Model model) {
		try {
			StringBuffer sb = new StringBuffer();
			Connection connection = getConnection();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Customer";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			List<Customer> customers = new ArrayList<Customer>();
			while (rs.next()) {
				int id = rs.getInt("id");
				String first = rs.getString("first_name");
				String last = rs.getString("last_name");
				String phone = rs.getString("phone_number");
				
				customers.add(new Customer(first, last, phone));
			}
			model.addAttribute("users", customers.get(0));
			return "appointmentCalendar";
		} catch (Exception e) {
			return e.toString();
		}
	}

	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	public String createCustomer(@ModelAttribute Customer customer, Model model) {
		model.addAttribute("customer", customer);
		
		long id = customer.getId();
		String first = customer.getFirstName();
		String last = customer.getLastName();
		String phone = customer.getPhoneNumber();
		
		try {
			Connection connection = getConnection();
			Statement stmt = connection.createStatement();
			String sql;
			sql = "INSERT INTO Customers(id, first_name, last_name, phone_number) values ('null, " + first + "', '" + last
					+ " ',' " + phone + "');";
			ResultSet rs = stmt.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "result";
	}

	private static Connection getConnection() throws URISyntaxException, SQLException {
		URI dbUri = null;
		String DATABASE_URL = "postgres://pvmwwbiarkromb:141bb82b3434290fa6d0c8075f42505a8eb8f018308ddbeaf27fb73c68eeb283@ec2-23-21-238-246.compute-1.amazonaws.com:5432/df8emisrshb38e";
		dbUri = new URI(DATABASE_URL);

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()
				+ "?sslmode=require";

		return DriverManager.getConnection(dbUrl, username, password);
	}
}