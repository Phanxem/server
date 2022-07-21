package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class UserProfileImageUpdateFailureException extends ServerException{
	private static final long CODE = 401;
	private static final String MESSAGE = "";
	
	public UserProfileImageUpdateFailureException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
}
