package com.natour.server.data.dtos;

import org.springframework.http.HttpStatus;

public class ResponseDTO {

	private String message;
	private HttpStatus resultCode;
	
	public ResponseDTO(String message, HttpStatus resultCode) {
		this.message = message;
		this.resultCode = resultCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getResultCode() {
		return resultCode;
	}

	public void setResultCode(HttpStatus resultCode) {
		this.resultCode = resultCode;
	}
	
	
}
