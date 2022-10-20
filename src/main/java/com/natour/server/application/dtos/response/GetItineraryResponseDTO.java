package com.natour.server.application.dtos.response;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public class GetItineraryResponseDTO {

	private ResultMessageDTO resultMessage;
	
	private Long id;
	private String name;
	private Float duration;
	private Float lenght;
	private Integer difficulty;
	private String description;
	
	private Long idUser;
	
	
	

	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}

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
