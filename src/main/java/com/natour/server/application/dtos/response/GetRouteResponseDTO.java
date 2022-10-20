package com.natour.server.application.dtos.response;

import java.util.ArrayList;
import java.util.List;

import com.mapbox.services.commons.geojson.LineString;

public class GetRouteResponseDTO {
	
	private ResultMessageDTO resultMessage;
	
	private List<PointResponseDTO> wayPoints;
	private List<GetRouteLegResponseDTO> tracks;
	
	public GetRouteResponseDTO() {}
	
	public GetRouteResponseDTO(float distance, float duration, LineString geometry) {
	
		this.setWayPoints(new ArrayList<PointResponseDTO>());
		this.tracks = new ArrayList<GetRouteLegResponseDTO>();
	}

	
	
	
	
	
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}

	public List<GetRouteLegResponseDTO> getTracks() {
		return tracks;
	}

	public void setTracks(List<GetRouteLegResponseDTO> tracks) {
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
