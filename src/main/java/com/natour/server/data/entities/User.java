package com.natour.server.data.entities;


import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(nullable=false,unique=true)
	private String identityProvider;
	@Column(nullable=false,unique=true)
	private String idIdentityProvided;
	
	@Column(nullable=false,unique=true)
	private String username;
	private String profileImageURL;
	private String placeOfResidence;
	private Timestamp dateOfBirth;
	
	private boolean isFacebookLinked;
	private boolean isGoogleLinked;
	
	
	@OneToMany(mappedBy = "user")
    private List<Itinerary> itineraries;

	
	@ManyToMany
	@JoinTable(name = "UserChat",
			   joinColumns = @JoinColumn(name = "idUser"),
			   inverseJoinColumns = @JoinColumn(name = "idChat"))
	private List<Chat> chats;
	
	
	
	
	public User() {}
	
	public User(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfileImageURL() {
		return profileImageURL;
	}

	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPlaceOfResidence() {
		return placeOfResidence;
	}

	public void setPlaceOfResidence(String placeOfResidence) {
		this.placeOfResidence = placeOfResidence;
	}

	public Timestamp getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Timestamp dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	public List<Itinerary> getItineraries() {
		return itineraries;
	}

	public void setItineraries(List<Itinerary> itineraries) {
		this.itineraries = itineraries;
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

