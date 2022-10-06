package com.natour.server.application.dtos.response;

import java.util.List;

public class ListAddressResponseDTO {

	private ResultMessageDTO resultMessage;
	private List<AddressResponseDTO> listAddresses;
	
	
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<AddressResponseDTO> getListAddresses() {
		return listAddresses;
	}
	public void setListAddresses(List<AddressResponseDTO> listAddresses) {
		this.listAddresses = listAddresses;
	}
	
	
	
}
