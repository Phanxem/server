package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class UserProfileImageSaveFailureException extends ServerException{
	private static final long CODE = 500;
	private static final String MESSAGE = "";
	
	public UserProfileImageSaveFailureException(Exception e) {
		super(e);
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
	public UserProfileImageSaveFailureException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
		
	}
}
