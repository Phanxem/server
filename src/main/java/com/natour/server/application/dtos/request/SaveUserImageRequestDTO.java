package com.natour.server.application.dtos.request;

import org.springframework.web.multipart.MultipartFile;

public class SaveUserImageRequestDTO {

	private MultipartFile image;

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}
	
	
	
}
