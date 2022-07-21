package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class ReportNotFoundException extends ServerException{
	
	private static final long CODE = 102;
	private static final String MESSAGE = "";

	public ReportNotFoundException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}

}
