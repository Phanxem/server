package com.natour.server.application.exceptionHandler;

public class ServerException extends RuntimeException{
	
	private long code;
	private String message;
	
	public ServerException(){}
	
	public ServerException(Exception exception) {
		super(exception);
	}
	
	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
