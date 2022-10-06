package com.natour.server.application.dtos.response;

import java.util.List;

public class ListItineraryResponseDTO {

	private ResultMessageDTO resultMessage;
	private List<ItineraryResponseDTO> listItinerary;
	
	
	
	
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<ItineraryResponseDTO> getListItinerary() {
		return listItinerary;
	}
	public void setListItinerary(List<ItineraryResponseDTO> listItinerary) {
		this.listItinerary = listItinerary;
	}
	
	
	
}
