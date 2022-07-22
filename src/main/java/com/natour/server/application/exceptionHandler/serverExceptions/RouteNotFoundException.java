package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class RouteNotFoundException  extends ServerException{
	private static final long CODE = 501;
	private static final String MESSAGE = "";
	
	public RouteNotFoundException(Exception e) {
		super(e);
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
	public RouteNotFoundException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
		
	}

}
