package com.natour.server.application.dtos;

import java.sql.Date;

public class OptionalInfoUserDTO {

	private String placeOfResidence;
	private Date dateOfBirth;
	private String gender;
	
	public OptionalInfoUserDTO() {}
	
	public OptionalInfoUserDTO(String placeOfResidence, Date dateOfBirth, String gender) {
		this.placeOfResidence = placeOfResidence;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
	}
	
	public String getPlaceOfResidence() {
		return placeOfResidence;
	}
	public void setPlaceOfResidence(String placeOfResidence) {
		this.placeOfResidence = placeOfResidence;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
}
