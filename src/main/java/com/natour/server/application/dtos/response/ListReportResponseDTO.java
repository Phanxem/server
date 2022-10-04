package com.natour.server.application.dtos.response;

import java.util.List;

public class ListReportResponseDTO {

	private MessageResponseDTO resultMessage;
	private List<ReportResponseDTO> listReport;
	
	
	
	public MessageResponseDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(MessageResponseDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<ReportResponseDTO> getListReport() {
		return listReport;
	}
	public void setListReport(List<ReportResponseDTO> listReport) {
		this.listReport = listReport;
	}
	
	
	
}
