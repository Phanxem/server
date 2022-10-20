package com.natour.server.application.dtos.response;

import java.util.List;

public class GetListReportResponseDTO {

	private ResultMessageDTO resultMessage;
	private List<GetReportResponseDTO> listReport;
	
	
	
	public ResultMessageDTO getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessageDTO resultMessage) {
		this.resultMessage = resultMessage;
	}
	public List<GetReportResponseDTO> getListReport() {
		return listReport;
	}
	public void setListReport(List<GetReportResponseDTO> listReport) {
		this.listReport = listReport;
	}
	
	
	
}
