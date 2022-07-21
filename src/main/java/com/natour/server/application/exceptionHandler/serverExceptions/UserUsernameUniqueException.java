package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class UserUsernameUniqueException extends ServerException{

	private static final long CODE = 300;
	private static final String MESSAGE = "";
	
	public UserUsernameUniqueException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
}
