package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class RouteCoordinatesInvalidException  extends ServerException{
	private static final long CODE = 501;
	private static final String MESSAGE = "";
	
	public RouteCoordinatesInvalidException(Exception e) {
		super(e);
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
	public RouteCoordinatesInvalidException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
		
	}

}
