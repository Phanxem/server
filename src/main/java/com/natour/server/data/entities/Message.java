package com.natour.server.data.entities;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Message {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(nullable=false,unique=true)
	private String body;
	@Column(nullable=false,unique=true)
	private Timestamp dateOfInput;
	
	@ManyToOne
    @JoinColumn(name="idUser", nullable=false)
    private User user;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idChat", referencedColumnName = "id")
    private Chat chat;
	
}
