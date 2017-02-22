package com.hampson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hampson.model.Customer;
import com.hampson.salonapp.repository.CustomerRepository;

@RestController
public class CustomerRepositoryController {

	@Autowired
	CustomerRepository repository;

	@RequestMapping("/save")
	public String process() {
		repository.save(new Customer("Jack", "Smith", "555-666-7777"));
		repository.save(new Customer("Adam", "Johnson", "444-555-6666"));
		repository.save(new Customer("Kim", "Smith", "333-555-3333"));
		repository.save(new Customer("David", "Williams", "555-222-2343"));
		repository.save(new Customer("Peter", "Davis", "666-333-5555"));
		
		return "Done";
	}

	@RequestMapping("/findall")
	public String findAll() {
		String result = "<html>";

		for (Customer cust : repository.findAll()) {
			result += "<div>" + cust.toString() + "</div>";
		}

		return result + "</html>";
	}

	@RequestMapping("/findbyid")
	public String findById(@RequestParam("id") long id) {
		String result = "";
		result = repository.findOne(id).toString();
		return result;
	}

	@RequestMapping("/findbylastname")
	public String fetchDataByLastName(@RequestParam("lastname") String lastName) {
		String result = "<html>";

		for (Customer cust : repository.findByLastName(lastName)) {
			result += "<div>" + cust.toString() + "</div>";
		}

		return result + "</html>";
	}
}
