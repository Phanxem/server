package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class AddressNotFoundException extends ServerException{
	private static final long CODE = 700;
	private static final String MESSAGE = "";
	
	public AddressNotFoundException(Exception e) {
		super(e);
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
	public AddressNotFoundException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
		
	}
}
