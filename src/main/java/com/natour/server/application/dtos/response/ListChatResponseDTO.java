package com.natour.server.application.dtos.response;

import java.util.List;

public class ListChatResponseDTO {

	private ResultMessageDTO resultMessage;
	private List<ChatResponseDTO> listChat;
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<ChatResponseDTO> getListChat() {
		return listChat;
	}
	public void setListChat(List<ChatResponseDTO> listChat) {
		this.listChat = listChat;
	}
	
	
	
}