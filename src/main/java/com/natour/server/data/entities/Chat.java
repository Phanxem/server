package com.natour.server.data.entities;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

public class Chat {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	 @OneToOne(mappedBy = "chat")
	 private Message message;
	 
	 @ManyToMany(mappedBy = "chats")
	 private List<User> user;
}
