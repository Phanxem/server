package com.natour.server.application.dtos.response;

import java.util.List;

public class ListChatResponseDTO {

	private ResultMessageDTO resultMessage;
	private List<Chat2ResponseDTO> listChat;
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<Chat2ResponseDTO> getListChat() {
		return listChat;
	}
	public void setListChat(List<Chat2ResponseDTO> listChat) {
		this.listChat = listChat;
	}
	
	
	
}
