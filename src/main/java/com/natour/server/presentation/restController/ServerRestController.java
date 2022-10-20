package com.natour.server.presentation.restController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.services.utils.ResultMessageUtils;

@RestController
@RequestMapping(value="/server")
public class ServerRestController {

	//Test
		@RequestMapping(value="/test", method=RequestMethod.GET)
		@ResponseBody
		public ResponseEntity<ResultMessageDTO> test(){
			System.out.println("TEST: test");
			
			return new ResponseEntity<ResultMessageDTO>(ResultMessageUtils.SUCCESS_MESSAGE, HttpStatus.OK);
		}	
}
