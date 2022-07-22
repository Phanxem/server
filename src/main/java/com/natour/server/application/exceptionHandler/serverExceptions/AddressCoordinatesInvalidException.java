package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class AddressCoordinatesInvalidException extends ServerException{
	private static final long CODE = 501;
	private static final String MESSAGE = "";
	
	public AddressCoordinatesInvalidException(Exception e) {
		super(e);
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
	public AddressCoordinatesInvalidException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
		
	}

}
