package com.natour.server.application.dtos.request;

import java.util.Date;

public class ReportRequestDTO {
	private Long id;
	private String name;
	private String dateOfInput;
	private String description;
	
	private Long idUser;
	private Long idItinerary;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDateOfInput() {
		return dateOfInput;
	}
	public void setDateOfInput(String dateOfInput) {
		this.dateOfInput = dateOfInput;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getIdUser() {
		return idUser;
	}
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}
	public Long getIdItinerary() {
		return idItinerary;
	}
	public void setIdItinerary(Long idItinerary) {
		this.idItinerary = idItinerary;
	}

}
