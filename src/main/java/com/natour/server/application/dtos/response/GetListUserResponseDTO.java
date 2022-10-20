package com.natour.server.application.dtos.response;

import java.util.List;

public class GetListUserResponseDTO {

	private ResultMessageDTO resultMessage;
	private List<GetUserResponseDTO> listUser;
	
	
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<GetUserResponseDTO> getListUser() {
		return listUser;
	}
	public void setListUser(List<GetUserResponseDTO> listUser) {
		this.listUser = listUser;
	}
	
	
	
	
}
