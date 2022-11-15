package com.natour.server.application.dtos.response;

public class HasMessageToReadResponseDTO {
	
	private ResultMessageDTO resultMessage;
	private boolean hasMessageToRead;

	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}

	public boolean isHasMessageToRead() {
		return hasMessageToRead;
	}

	public void setHasMessageToRead(boolean hasMessageToRead) {
		this.hasMessageToRead = hasMessageToRead;
	}

	
}
