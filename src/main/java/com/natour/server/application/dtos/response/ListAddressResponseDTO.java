package com.natour.server.application.dtos.response;

import java.util.List;

public class ListAddressResponseDTO {

	private MessageResponseDTO resultMessage;
	private List<AddressResponseDTO> listAddresses;
	
	
	public MessageResponseDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(MessageResponseDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<AddressResponseDTO> getListAddresses() {
		return listAddresses;
	}
	public void setListAddresses(List<AddressResponseDTO> listAddresses) {
		this.listAddresses = listAddresses;
	}
	
	
	
}
