package com.hampson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.hampson.dao.AppointmentDAO;

@SpringBootApplication
public class SalonAppSpringBootApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SalonAppSpringBootApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SalonAppSpringBootApplication.class, args);
	}
	
	@Bean
	public AppointmentDAO appointmentDAO() {
	    return new AppointmentDAO();
	}
	
    @Bean
    public RestTemplate geRestTemplate() {
        return new RestTemplate();
    }
}
