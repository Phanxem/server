package com.natour.server.application.dtos.response;

import java.util.List;

public class ListUserResponseDTO {

	private ResultMessageDTO resultMessage;
	private List<UserResponseDTO> listUser;
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<UserResponseDTO> getListUser() {
		return listUser;
	}
	public void setListUser(List<UserResponseDTO> listUser) {
		this.listUser = listUser;
	}
	
	
	
	
}
