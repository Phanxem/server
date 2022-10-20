package com.natour.server.application.dtos.response;

import java.util.List;

public class GetRouteLegResponseDTO {
	private PointResponseDTO startingPoint;
	private PointResponseDTO destinationPoint;
	
	private List<PointResponseDTO> track;
	
	private float duration;
	private float distance;

	

	public PointResponseDTO getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(PointResponseDTO startingPoint) {
		this.startingPoint = startingPoint;
	}

	public PointResponseDTO getDestinationPoint() {
		return destinationPoint;
	}

	public void setDestinationPoint(PointResponseDTO destinationPoint) {
		this.destinationPoint = destinationPoint;
	}

	public List<PointResponseDTO> getTrack() {
		return track;
	}

	public void setTrack(List<PointResponseDTO> track) {
		this.track = track;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	
}
