package com.natour.server.data.entities.rds;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Chat{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	 @OneToMany(mappedBy = "chat")
	 private List<Message> messages;
	 
	 @ManyToMany(mappedBy = "chats")
	 private List<User> users;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	
	 @Override
	 public boolean equals(Object o) {
		 if (o == this) return true;	        
		 if (!(o instanceof Chat) ) return false;
	        
	     Chat chat = (Chat) o;   
	     long idChat = chat.getId();
	     
	     return idChat == this.id;
	}
}
