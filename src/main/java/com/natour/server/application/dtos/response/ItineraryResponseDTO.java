package com.natour.server.application.dtos.response;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public class ItineraryResponseDTO {

	private Long id;
	private String name;
	private Resource gpx;
	private Float duration;
	private Float lenght;
	private Integer difficulty;
	private String description;
	
	private Long idUser;
	
	
	

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



	public Resource getGpx() {
		return gpx;
	}

	public void setGpx(Resource gpx) {
		this.gpx = gpx;
	}

	public Float getDuration() {
		return duration;
	}

	public void setDuration(Float duration) {
		this.duration = duration;
	}

	public Float getLenght() {
		return lenght;
	}

	public void setLenght(Float lenght) {
		this.lenght = lenght;
	}

	public Integer getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
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

	
}
