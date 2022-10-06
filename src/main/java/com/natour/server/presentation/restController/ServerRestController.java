package com.natour.server.presentation.restController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.natour.server.application.dtos.response.ResultMessageDTO;

@RestController
@RequestMapping(value="/server")
public class ServerRestController {

	//Test
		@RequestMapping(value="/test", method=RequestMethod.GET)
		@ResponseBody
		public ResponseEntity<ResultMessageDTO> test(){
			System.out.println("TEST: test");
			
			ResultMessageDTO resultMessage = new ResultMessageDTO();
			resultMessage.setMessage("Il server è attualmente in funzione");
			return new ResponseEntity<ResultMessageDTO>(resultMessage, HttpStatus.OK);
		}	
}
