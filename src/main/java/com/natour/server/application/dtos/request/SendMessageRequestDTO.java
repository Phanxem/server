package com.natour.server.application.dtos.request;

public class SendMessageRequestDTO {
	
	private String idConnectionDestination;
	private long idUserSource;
	private String message;
	private String inputTime;
	public String getIdConnectionDestination() {
		return idConnectionDestination;
	}
	public void setIdConnectionDestination(String idConnectionDestination) {
		this.idConnectionDestination = idConnectionDestination;
	}
	public long getIdUserSource() {
		return idUserSource;
	}
	public void setIdUserSource(long idUserSource) {
		this.idUserSource = idUserSource;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getInputTime() {
		return inputTime;
	}
	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}
	
	
	
}
