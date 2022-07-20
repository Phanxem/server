package com.natour.server.application.dtos;


import java.util.Date;

public class ReportDTO {

	private long id;
	private String name;
	private Date dateOfInput;
	private String description;
	
	private long idUser;
	private long idItinerary;
	
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
	public Date getDateOfInput() {
		return dateOfInput;
	}
	public void setDateOfInput(Date dateOfInput) {
		this.dateOfInput = dateOfInput;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getIdUser() {
		return idUser;
	}
	public void setIdUser(long idUser) {
		this.idUser = idUser;
	}
	public long getIdItinerary() {
		return idItinerary;
	}
	public void setIdItinerary(long idItinerary) {
		this.idItinerary = idItinerary;
	}
	
	
	
	
}
