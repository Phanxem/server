package com.natour.server.application.dtos;

import java.util.List;

public class RouteLegDTO {
	private PointDTO startingPoint;
	private PointDTO destinationPoint;
	
	private List<PointDTO> track;
	
	private float duration;
	private float distance;

	

	public PointDTO getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(PointDTO startingPoint) {
		this.startingPoint = startingPoint;
	}

	public PointDTO getDestinationPoint() {
		return destinationPoint;
	}

	public void setDestinationPoint(PointDTO destinationPoint) {
		this.destinationPoint = destinationPoint;
	}

	public List<PointDTO> getTrack() {
		return track;
	}

	public void setTrack(List<PointDTO> track) {
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
