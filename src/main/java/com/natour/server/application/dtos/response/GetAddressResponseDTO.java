package com.natour.server.application.dtos.response;

public class GetAddressResponseDTO {

	private ResultMessageDTO resultMessage;
	
	private PointResponseDTO point;
	private String addressName;
	
	public GetAddressResponseDTO() {
		this.point = new PointResponseDTO();
	}
	


	
	
	
	
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}







	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}







	public PointResponseDTO getPoint() {
		return point;
	}

	public void setPoint(PointResponseDTO point) {
		this.point = point;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}


	

	
	
	
}
