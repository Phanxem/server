package com.natour.server.application.dtos.response;

import java.util.List;

public class ListItineraryResponseDTO {

	private MessageResponseDTO resultMessage;
	private List<ItineraryResponseDTO> listItinerary;
	
	
	
	
	public MessageResponseDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(MessageResponseDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<ItineraryResponseDTO> getListItinerary() {
		return listItinerary;
	}
	public void setListItinerary(List<ItineraryResponseDTO> listItinerary) {
		this.listItinerary = listItinerary;
	}
	
	
	
}
