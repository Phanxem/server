package com.natour.server.application.dtos.response;

import org.springframework.core.io.Resource;

public class ResourceResponseDTO {

	private ResultMessageDTO resultMessage;

	private Resource resource;

	

	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	
	
}
