package com.natour.server.data.entities.rds;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Itinerary {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private String gpxURL;
	@Column(nullable=false)
	private Float duration;
	@Column(nullable=false)
	private Float lenght;
	@Column(nullable=false)
	private Integer difficulty;
	private String description;
	
	@ManyToOne
    @JoinColumn(name="idUser", nullable=false)
    private User user;
	
	
	public Itinerary() {}
	
	
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
	
	public String getGpxURL() {
		return gpxURL;
	}


	public void setGpxURL(String gpxURL) {
		this.gpxURL = gpxURL;
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


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
}
