package com.natour.server.application.dtos;

import java.time.Duration;

import io.jenetics.jpx.GPX;

public class ItineraryDTO {

	private long id;
	private String name;
	private GPX wayPoints;
	private Duration duration;
	private float lenght;
	private int difficulty;
	private String description;
	
	private long idUser;
	
	
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

	public GPX getWayPoints() {
		return wayPoints;
	}

	public void setWayPoints(GPX wayPoints) {
		this.wayPoints = wayPoints;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public float getLenght() {
		return lenght;
	}

	public void setLenght(float lenght) {
		this.lenght = lenght;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
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
	
	

}
