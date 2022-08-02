package com.natour.server.application.dtos.request;

public class OptionalInfoUserRequestDTO {

	private String placeOfResidence;
	private String dateOfBirth;
	
	public OptionalInfoUserRequestDTO() {}
	
	public OptionalInfoUserRequestDTO(String placeOfResidence, String dateOfBirth) {
		this.placeOfResidence = placeOfResidence;
		this.dateOfBirth = dateOfBirth;

	}
	
	public String getPlaceOfResidence() {
		return placeOfResidence;
	}
	public void setPlaceOfResidence(String placeOfResidence) {
		this.placeOfResidence = placeOfResidence;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	
	
}