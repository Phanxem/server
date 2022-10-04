package com.natour.server.presentation.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.natour.server.application.dtos.response.MessageResponseDTO;
import com.natour.server.application.dtos.response.RouteResponseDTO;
import com.natour.server.application.services.ResultCodeUtils;
import com.natour.server.application.services.RouteService;

@RestController
@RequestMapping(value="/route")
public class RouteRestController {

	@Autowired
	private RouteService routeService;
	
	//GETs
	@RequestMapping(value="/get/{coordinates}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<RouteResponseDTO> getRouteByCoordinates(@PathVariable("coordinates") String coordinates){
		System.out.println("TEST: GET route");
		System.out.println(coordinates);
		
		RouteResponseDTO result = routeService.findRouteByCoordinates(coordinates);
		MessageResponseDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<RouteResponseDTO>(result, resultHttpStatus);
	}	
}
