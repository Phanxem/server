package com.natour.server.application.dtos.response;

import java.util.List;

public class GetListAddressResponseDTO {

	private ResultMessageDTO resultMessage;
	private List<GetAddressResponseDTO> listAddress;
	
	
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<GetAddressResponseDTO> getListAddress() {
		return listAddress;
	}
	public void setListAddress(List<GetAddressResponseDTO> listAddress) {
		this.listAddress = listAddress;
	}
	
	
	
}
