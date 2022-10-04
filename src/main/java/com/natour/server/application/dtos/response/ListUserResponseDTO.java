package com.natour.server.application.dtos.response;

import java.util.List;

public class ListUserResponseDTO {

	private MessageResponseDTO resultMessage;
	private List<UserResponseDTO> listUser;
	public MessageResponseDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(MessageResponseDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<UserResponseDTO> getListUser() {
		return listUser;
	}
	public void setListUser(List<UserResponseDTO> listUser) {
		this.listUser = listUser;
	}
	
	
	
	
}
