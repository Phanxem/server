package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class TODORouteException extends ServerException{
	private static final long CODE = 10002;
	private static final String MESSAGE = "TODO impossibile trovare route";
	
	public TODORouteException(Exception e) {
		super(e);
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
	public TODORouteException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
		
	}
}
