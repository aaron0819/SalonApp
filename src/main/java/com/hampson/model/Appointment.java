package com.hampson.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment implements Serializable {

	private static final long serialVersionUID = -3277572530717692763L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
     
    @Column(name = "stylist_id")
    private int stylistId;
     
    @Column(name = "customer_id")
    private int customerId;
    
    @Column(name = "time")
    private String time;
 
    protected Appointment() {}
 
    public Appointment(int stylistId, int customerId, String time) {
    	this.stylistId = stylistId;
    	this.customerId = customerId;
    	this.time = time;
    }
 
    @Override
    public String toString() {
        return String.format(
                "Appointment[id=%d, stylist ID=%d, customer ID=%d, time='%s']",
                id, stylistId, customerId, time);
    }

}
