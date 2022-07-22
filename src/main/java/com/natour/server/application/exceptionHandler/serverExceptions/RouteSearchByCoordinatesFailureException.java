package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class RouteSearchByCoordinatesFailureException  extends ServerException{
	private static final long CODE = 501;
	private static final String MESSAGE = "";
	
	public RouteSearchByCoordinatesFailureException(Exception e) {
		super(e);
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
	public RouteSearchByCoordinatesFailureException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
		
	}

}
