package com.natour.server.application.dtos;

public class SuccessMessageDTO extends MessageDTO{

	private static final long CODE = 000;
	private static final String MESSAGE = "Operazione effettuata con successo";
	
	public SuccessMessageDTO() {
		//super();
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
}
