package com.natour.server.data.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Report {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private Timestamp dateOfInput;
	private String description;
	
	
	@ManyToOne
    @JoinColumn(name="idUser", nullable=false)
    private User user;
	
	@ManyToOne
    @JoinColumn(name="idItinerary", nullable=false)
    private Itinerary itinerary;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getDateOfInput() {
		return dateOfInput;
	}
	public void setDateOfInput(Timestamp dateOfInput) {
		this.dateOfInput = dateOfInput;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
