package com.natour.server.application.dtos.response;

import java.util.List;

public class GetListItineraryResponseDTO {

	private ResultMessageDTO resultMessage;
	private List<GetItineraryResponseDTO> listItinerary;
	
	
	
	
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<GetItineraryResponseDTO> getListItinerary() {
		return listItinerary;
	}
	public void setListItinerary(List<GetItineraryResponseDTO> listItinerary) {
		this.listItinerary = listItinerary;
	}
	
	
	
}
