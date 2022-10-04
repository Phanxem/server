package com.natour.server.application.dtos.response;

public class AddressResponseDTO {

	private MessageResponseDTO resultMessage;
	
	private PointResponseDTO point;
	private String addressLine;
	
	public AddressResponseDTO() {
		this.point = new PointResponseDTO();
	}
	


	
	
	
	
	public MessageResponseDTO getResultMessage() {
		return resultMessage;
	}







	public void setResultMessage(MessageResponseDTO resultMessage) {
		this.resultMessage = resultMessage;
	}







	public PointResponseDTO getPoint() {
		return point;
	}

	public void setPoint(PointResponseDTO point) {
		this.point = point;
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}


	

	
	
	
}
