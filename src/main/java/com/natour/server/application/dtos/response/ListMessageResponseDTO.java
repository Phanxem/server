package com.natour.server.application.dtos.response;

import java.util.List;

public class ListMessageResponseDTO {

	private ResultMessageDTO resultMessage;
	private List<MessageResponseDTO> listMessage;
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<MessageResponseDTO> getListMessage() {
		return listMessage;
	}
	public void setListMessage(List<MessageResponseDTO> listMessage) {
		this.listMessage = listMessage;
	}
	
	
	
}
