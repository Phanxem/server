package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class FileConvertionFailureException extends ServerException{
	private static final long CODE = 506;
	private static final String MESSAGE = "";
	
	public FileConvertionFailureException(Exception e) {
		super(e);
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
	public FileConvertionFailureException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
		
	}

}
