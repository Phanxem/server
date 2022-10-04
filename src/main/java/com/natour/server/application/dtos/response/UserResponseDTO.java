package com.natour.server.application.dtos.response;

public class UserResponseDTO {

	private MessageResponseDTO resultMessage;
	
	private long id;
	private String username;
	private String placeOfResidence;
	private String dateOfBirth;
	
	private boolean isFacebookLinked;
	private boolean isGoogleLinked;
	
	
	
	
	
	public MessageResponseDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(MessageResponseDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public boolean isFacebookLinked() {
		return isFacebookLinked;
	}
	public void setFacebookLinked(boolean isFacebookLinked) {
		this.isFacebookLinked = isFacebookLinked;
	}
	public boolean isGoogleLinked() {
		return isGoogleLinked;
	}
	public void setGoogleLinked(boolean isGoogleLinked) {
		this.isGoogleLinked = isGoogleLinked;
	}
	
	
	
	
	
	
}
