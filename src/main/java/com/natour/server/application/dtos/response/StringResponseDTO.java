package com.natour.server.application.dtos.response;

public class StringResponseDTO {
	private ResultMessageDTO resultMessage;
	
	private String string;

	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
	
	
}
