package com.natour.server.application.dtos;

import java.util.ArrayList;
import java.util.List;

import com.mapbox.services.commons.geojson.LineString;

public class RouteDTO {
	
	private List<PointDTO> wayPoints;
	private List<RouteLegDTO> tracks;
	
	//private List<PointDTO> geometry;
	
	public RouteDTO() {}
	
	public RouteDTO(float distance, float duration, LineString geometry) {

		//this.geometry = new ArrayList<PointDTO>();
	
		this.setWayPoints(new ArrayList<PointDTO>());
		this.tracks = new ArrayList<RouteLegDTO>();
	}

	public List<RouteLegDTO> getTracks() {
		return tracks;
	}

	public void setTracks(List<RouteLegDTO> tracks) {
		this.tracks = tracks;
	}

	public List<PointDTO> getWayPoints() {
		return wayPoints;
	}

	public void setWayPoints(List<PointDTO> wayPoints) {
		this.wayPoints = wayPoints;
	}
	
	
	
/*
	public List<PointDTO> getGeometry() {
		return geometry;
	}

	public void setGeometry(List<PointDTO> geometry) {
		this.geometry = geometry;
	}
*/

	
	
}
