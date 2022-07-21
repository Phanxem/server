package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class UserProfileImageNullException extends ServerException{

	private static final long CODE = 209;
	private static final String MESSAGE = "";
	
	public UserProfileImageNullException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}

}
