package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class TODOException extends ServerException{
	private static final long CODE = 204;
	private static final String MESSAGE = "";
	
	public TODOException(Exception e) {
		super(e);
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
	public TODOException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
		
	}
}
