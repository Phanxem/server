package com.natour.server.data.entities.rds;

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
public class Message implements Comparable<Message>{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(nullable=false)
	private String body;
	@Column(nullable=false)
	private Timestamp dateOfInput;
	
	@ManyToOne
    @JoinColumn(name="idUser", nullable=false)
    private User user;
	
	@ManyToOne
    @JoinColumn(name = "idChat", referencedColumnName = "id")
    private Chat chat;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Timestamp getDateOfInput() {
		return dateOfInput;
	}

	public void setDateOfInput(Timestamp dateOfInput) {
		this.dateOfInput = dateOfInput;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	@Override
	public int compareTo(Message o) {
		if(this.getDateOfInput() == null || o.getDateOfInput() == null) return 0;
		
		Timestamp thisDate = this.getDateOfInput();
		Timestamp otherDate = o.getDateOfInput();
		
		return thisDate.compareTo(otherDate);
	}
	
	
	
	
}
