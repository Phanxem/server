package com.natour.server.application.dtos.request;

import java.util.Map;

public class ChatRequestDTO {

	private String connectionId;
	private Map<String,String> payload;
	
	public String getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}
	public Map<String, String> getPayload() {
		return payload;
	}
	public void setPayload(Map<String, String> payload) {
		this.payload = payload;
	}
	
	
	
}
