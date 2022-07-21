package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class UserOptionalInfoUpdateFailureException extends ServerException{

	private static final long CODE = 400;
	private static final String MESSAGE = "";
	
	public UserOptionalInfoUpdateFailureException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
}
