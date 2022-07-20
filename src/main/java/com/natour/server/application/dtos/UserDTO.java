package com.natour.server.application.dtos;

import java.sql.Date;

import org.springframework.core.io.FileSystemResource;

public class UserDTO {


	private long id;
	private String username;
	private FileSystemResource profileImage;
	private String placeOfResidence;
	private Date dateOfBirth;
	private String gender;
	
	
	
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
	public FileSystemResource getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(FileSystemResource image) {
		this.profileImage = image;
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
