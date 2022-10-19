package com.natour.server.application.dtos.response;

public class PointResponseDTO {
	
	private ResultMessageDTO resultMessage;
	
	private double lon;
	private double lat;
	
	public PointResponseDTO() {}
	
	public PointResponseDTO(double lon, double lat) {
		this.lon = lon;
		this.lat = lat;
	}
	
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}

	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	
}
