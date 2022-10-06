package com.natour.server.application.dtos.response;

import java.util.ArrayList;
import java.util.List;

import com.mapbox.services.commons.geojson.LineString;

public class RouteResponseDTO {
	
	private ResultMessageDTO resultMessage;
	
	private List<PointResponseDTO> wayPoints;
	private List<RouteLegResponseDTO> tracks;
	
	//private List<PointDTO> geometry;
	
	public RouteResponseDTO() {}
	
	public RouteResponseDTO(float distance, float duration, LineString geometry) {
	
		this.setWayPoints(new ArrayList<PointResponseDTO>());
		this.tracks = new ArrayList<RouteLegResponseDTO>();
	}

	
	
	
	
	
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}

	public List<RouteLegResponseDTO> getTracks() {
		return tracks;
	}

	public void setTracks(List<RouteLegResponseDTO> tracks) {
		this.tracks = tracks;
	}

	public List<PointResponseDTO> getWayPoints() {
		return wayPoints;
	}

	public void setWayPoints(List<PointResponseDTO> wayPoints) {
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
