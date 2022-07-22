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
import com.natour.server.application.services.AddressService;

@RestController
@RequestMapping(value="/address")
public class AddressRestController {

	@Autowired
	private AddressService addressService;
	
	//GETs
	@RequestMapping(value="/get/{coordinates}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<AddressDTO> getAddressByCoordinates(@PathVariable("coordinates") String coordinates){
		System.out.println("TEST: GET coordinates");
		
		AddressDTO result = addressService.findAddressByCoordinates(coordinates);
		return new ResponseEntity<AddressDTO>(result, HttpStatus.OK);
	}
	
			
	//SEARCH
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<AddressDTO>> searchAddressByQuery(@RequestParam("query") String query){
		System.out.println("TEST: SEARCH query");
		
		List<AddressDTO> result = addressService.searchAddressesByQuery(query);
		return new ResponseEntity<List<AddressDTO>>(result, HttpStatus.OK);
		
	}
		
		
	
}
