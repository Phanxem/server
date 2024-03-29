package com.natour.server.presentation.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.GetRouteResponseDTO;
import com.natour.server.application.services.RouteService;
import com.natour.server.application.services.utils.ResultMessageUtils;

@RestController
@RequestMapping(value="/route")
public class RouteRestController {

	@Autowired
	private RouteService routeService;
	
	//GETs
	@RequestMapping(value="/get/{coordinates}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetRouteResponseDTO> getRouteByCoordinates(@PathVariable("coordinates") String coordinates){
		System.out.println("TEST: GET route");
		System.out.println(coordinates);
		
		GetRouteResponseDTO result = routeService.findRouteByCoordinates(coordinates);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetRouteResponseDTO>(result, resultHttpStatus);
	}	
}
