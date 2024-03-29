package com.natour.server.application.dtos.response;

public class ResultMessageDTO {
	private long code;
	private String message;
	
	public ResultMessageDTO(long code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
}
