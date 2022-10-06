package com.natour.server.application.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.ItineraryNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserProfileImageSaveFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserUsernameNullException;
import com.natour.server.application.exceptionHandler.serverExceptions.ReportNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.TODORouteException;

@ControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler({UserNotFoundException.class,
					   ItineraryNotFoundException.class, 
					   ReportNotFoundException.class})
    public ResponseEntity<ResultMessageDTO> handleNotFoundException(ServerException ex) {
        ResultMessageDTO errorDTO = new ResultMessageDTO(ex.getCode(), ex.getMessage());
		return new ResponseEntity<ResultMessageDTO>(errorDTO, HttpStatus.NOT_FOUND);
    }
	
	
	@ExceptionHandler({UserUsernameNullException.class})
		public ResponseEntity<ResultMessageDTO> handleNullException(ServerException ex) {
		ResultMessageDTO errorDTO = new ResultMessageDTO(ex.getCode(), ex.getMessage());
		return new ResponseEntity<ResultMessageDTO>(errorDTO, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler({UserProfileImageSaveFailureException.class})
	public ResponseEntity<ResultMessageDTO> handleUpdateFailureException(ServerException ex) {
		ResultMessageDTO errorDTO = new ResultMessageDTO(ex.getCode(), ex.getMessage());
		return new ResponseEntity<ResultMessageDTO>(errorDTO, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({TODORouteException.class})
	public ResponseEntity<ResultMessageDTO> handleTODOException(ServerException ex) {
		ResultMessageDTO errorDTO = new ResultMessageDTO(ex.getCode(), ex.getMessage());
		return new ResponseEntity<ResultMessageDTO>(errorDTO, HttpStatus.BAD_REQUEST);
	}
	
	
	
	//---
	/*
	@ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<MessageDTO> handleUserNotFoundException(UserNotFoundException ex) {
        MessageDTO errorDTO = new MessageDTO(ex.getCode(), ex.getMessage());
		return new ResponseEntity<MessageDTO>(errorDTO, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(value = ItineraryNotFoundException.class)
    public ResponseEntity<MessageDTO> handleItineraryNotFoundException(ItineraryNotFoundException ex) {
        MessageDTO errorDTO = new MessageDTO(ex.getCode(), ex.getMessage());
		return new ResponseEntity<MessageDTO>(errorDTO, HttpStatus.NOT_FOUND);
    }
	*/
}
