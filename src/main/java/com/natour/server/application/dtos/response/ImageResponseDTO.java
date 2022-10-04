package com.natour.server.application.dtos.response;

import org.springframework.core.io.Resource;

public class ImageResponseDTO {

	private MessageResponseDTO resultMessage;

	private Resource image;

	

	public MessageResponseDTO getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(MessageResponseDTO resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Resource getImage() {
		return image;
	}

	public void setImage(Resource image) {
		this.image = image;
	}
	
	
	
}
