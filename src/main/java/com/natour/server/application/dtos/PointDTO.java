package com.natour.server.application.dtos;

public class PointDTO {
	private double lon;
	private double lat;
	
	public PointDTO() {}
	
	public PointDTO(double lon, double lat) {
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
	
	
}
