package com.natour.server.presentation.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.natour.server.application.dtos.AddressDTO;
import com.natour.server.application.dtos.RouteDTO;
import com.natour.server.application.services.AddressService;
import com.natour.server.application.services.RouteService;

@RestController
@RequestMapping(value="/route")
public class RouteRestController {

	@Autowired
	private RouteService routeService;
	
	//GETs
	@RequestMapping(value="/get/{coordinates}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<RouteDTO> getRouteByCoordinates(@PathVariable("coordinates") String coordinates){
		System.out.println("TEST: GET coordinates");
		
		RouteDTO result = routeService.findRouteByCoordinates(coordinates);
		return new ResponseEntity<RouteDTO>(result, HttpStatus.OK);
	}
	
			

	
}
