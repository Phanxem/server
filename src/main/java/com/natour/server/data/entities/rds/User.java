package com.natour.server.data.entities.rds;


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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"identityProvider", "idIdentityProvided"}))
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String identityProvider;
	@Column(nullable=false,unique=true)
	private String idIdentityProvided;
	
	@Column(nullable=false)
	private String username;
	private String profileImageKey;
	private String placeOfResidence;
	private Timestamp dateOfBirth;
	
	@OneToMany(mappedBy = "user")
    private List<Itinerary> itineraries;
	
	@ManyToMany
	@JoinTable(name = "UserChat",
			   joinColumns = @JoinColumn(name = "idUser"),
			   inverseJoinColumns = @JoinColumn(name = "idChat"))
	private List<Chat> chats;
	
	
	
	public User() {}
	
	public User(String identityProvider, String idIdentityProvided, String username) {
		this.identityProvider = identityProvider;
		this.idIdentityProvided = idIdentityProvided;
		this.username = username;
	}
	
	public String getIdentityProvider() {
		return identityProvider;
	}

	public void setIdentityProvider(String identityProvider) {
		this.identityProvider = identityProvider;
	}

	public String getIdIdentityProvided() {
		return idIdentityProvided;
	}

	public void setIdIdentityProvided(String idIdentityProvided) {
		this.idIdentityProvided = idIdentityProvided;
	}

	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfileImageKey() {
		return profileImageKey;
	}

	public void setProfileImageKey(String profileImageKey) {
		this.profileImageKey = profileImageKey;
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


	
	
}

