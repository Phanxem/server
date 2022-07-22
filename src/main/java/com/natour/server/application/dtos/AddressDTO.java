package com.natour.server.application.dtos;

public class AddressDTO {

	private PointDTO point;
	private String addressLine;
	
	public AddressDTO() {
		this.point = new PointDTO();
	}
	
	public AddressDTO(PointDTO point, String addressLine) {
		this.point = point;
	}

	public PointDTO getPoint() {
		return point;
	}

	public void setPoint(PointDTO point) {
		this.point = point;
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}


	

	
	
	
}
