package com.natour.server.application.dtos.response;

import org.springframework.core.io.Resource;

public class ImageResponseDTO {

	private ResultMessageDTO resultMessage;

	private Resource image;

	

	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Resource getImage() {
		return image;
	}

	public void setImage(Resource image) {
		this.image = image;
	}
	
	
	
}
