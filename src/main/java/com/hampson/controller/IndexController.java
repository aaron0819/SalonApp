package com.hampson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hampson.dao.AppointmentDAO;

@Controller
public class IndexController {
	
	@Autowired
	private AppointmentDAO appointmentDAO;
	
	
    @RequestMapping("/")
    public String index(Model model) {
    	
    	model.addAttribute("appointments", appointmentDAO.getAppointments());
    	
        return "index";
    }
}
