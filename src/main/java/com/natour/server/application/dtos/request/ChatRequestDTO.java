package com.natour.server.application.dtos.request;

import java.util.Map;

public class ChatRequestDTO {

	private String idConnection;
	private Map<String,String> payload;
	
	public String getIdConnection() {
		return idConnection;
	}
	public void setIdConnection(String connectionId) {
		this.idConnection = connectionId;
	}
	public Map<String, String> getPayload() {
		return payload;
	}
	public void setPayload(Map<String, String> payload) {
		this.payload = payload;
	}
	
	
	
}
