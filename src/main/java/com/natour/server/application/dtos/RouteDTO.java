package com.natour.server.application.dtos;

import java.util.ArrayList;
import java.util.List;

import com.mapbox.services.commons.geojson.LineString;

public class RouteDTO {
	private float distance;
	private float duration;
	private List<PointDTO> geometry;
	
	public RouteDTO() {}
	
	public RouteDTO(float distance, float duration, LineString geometry) {
		this.distance = distance;
		this.duration = duration;
		this.geometry = new ArrayList<PointDTO>();
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public List<PointDTO> getGeometry() {
		return geometry;
	}

	public void setGeometry(List<PointDTO> geometry) {
		this.geometry = geometry;
	}


	
	
}
