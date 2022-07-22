package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class AddressSearchByQueryFailureException extends ServerException{
	private static final long CODE = 501;
	private static final String MESSAGE = "";
	
	public AddressSearchByQueryFailureException(Exception e) {
		super(e);
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
	public AddressSearchByQueryFailureException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
		
	}
}
