package com.natour.server.application.services.utils;

import org.springframework.http.HttpStatus;

import com.natour.server.application.dtos.response.ResultMessageDTO;

public class ResultMessageUtils {
	
	public static final ResultMessageDTO SUCCESS_MESSAGE = new ResultMessageDTO(200,"");
	
	public static final ResultMessageDTO ERROR_MESSAGE_INVALID_REQUEST = new ResultMessageDTO(400,"");
	public static final ResultMessageDTO ERROR_MESSAGE_FAILURE = new ResultMessageDTO(500,"");
	public static final ResultMessageDTO ERROR_MESSAGE_NOT_FOUND = new ResultMessageDTO(404,"");
	public static final ResultMessageDTO ERROR_MESSAGE_UNIQUE_VIOLATION = new ResultMessageDTO(402,"");

	
	public static boolean isSuccess(ResultMessageDTO resultMessage) {
		long code = resultMessage.getCode();
		long successCode = SUCCESS_MESSAGE.getCode();
		
		return code == successCode;
	}
	
	
	public static HttpStatus toHttpStatus(ResultMessageDTO resultMessage) {

		if(resultMessage.equals(SUCCESS_MESSAGE)) {
			return HttpStatus.OK;
		}
		if(resultMessage.equals(ERROR_MESSAGE_INVALID_REQUEST)) {
			return HttpStatus.BAD_REQUEST;
		}
		if(resultMessage.equals(ERROR_MESSAGE_FAILURE)) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		if(resultMessage.equals(ERROR_MESSAGE_NOT_FOUND)) {
			return HttpStatus.NOT_FOUND;
		}
		if(resultMessage.equals(ERROR_MESSAGE_UNIQUE_VIOLATION)) {
			return HttpStatus.BAD_REQUEST;
		}

		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
}

