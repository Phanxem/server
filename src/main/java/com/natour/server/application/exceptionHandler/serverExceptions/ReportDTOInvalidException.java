package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class ReportDTOInvalidException extends ServerException{
	
	private static final long CODE = 601;
	private static final String MESSAGE = "";
	
	public ReportDTOInvalidException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	

}
