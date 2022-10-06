package com.natour.server.application.dtos.response;

import java.util.List;

public class ListReportResponseDTO {

	private ResultMessageDTO resultMessage;
	private List<ReportResponseDTO> listReport;
	
	
	
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<ReportResponseDTO> getListReport() {
		return listReport;
	}
	public void setListReport(List<ReportResponseDTO> listReport) {
		this.listReport = listReport;
	}
	
	
	
}
