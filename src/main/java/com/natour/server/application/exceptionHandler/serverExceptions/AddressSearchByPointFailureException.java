package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class AddressSearchByPointFailureException extends ServerException{
	private static final long CODE = 501;
	private static final String MESSAGE = "";
	
	public AddressSearchByPointFailureException(Exception e) {
		super(e);
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
	public AddressSearchByPointFailureException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
		
	}

}
