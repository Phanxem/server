package com.natour.server.application.dtos.response;

public class ChatResponseDTO {

	private ResultMessageDTO resultMessage;
	
	private long id;
	
	private long idUser;
	private String nameChat;
	private String lastMessage;
	
	
	
	
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdUser() {
		return idUser;
	}
	public void setIdUser(long idUser) {
		this.idUser = idUser;
	}
	public String getNameChat() {
		return nameChat;
	}
	public void setNameChat(String nameChat) {
		this.nameChat = nameChat;
	}
	public String getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}
	
	
	
}
