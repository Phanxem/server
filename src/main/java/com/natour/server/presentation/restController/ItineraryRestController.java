package com.natour.server.presentation.restController;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.natour.server.application.dtos.ItineraryDTO;
import com.natour.server.application.services.ItineraryService;
import com.natour.server.application.services.UserService;


@RestController
@RequestMapping(value="/itinerary")
public class ItineraryRestController {

	@Autowired
	private ItineraryService itineraryService;
	@Autowired
	private UserService userService;
	
	//GETs
	@RequestMapping(value="/get/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ItineraryDTO> getItineraryById(@PathVariable("id") long id){
		System.out.println("TEST: GET id");
		
		ItineraryDTO result = itineraryService.findItineraryById(id);
		if(result != null) return new ResponseEntity<ItineraryDTO>(result, HttpStatus.OK);
		return new ResponseEntity<ItineraryDTO>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
		
	//---
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ItineraryDTO>> getItineraryByIdUser(@RequestParam(required = true) long idUser){
		System.out.println("TEST: GET idUser");
		
		List<ItineraryDTO> result = itineraryService.findItineraryByIdUser(idUser);
		if(result != null) return new ResponseEntity<List<ItineraryDTO>>(result, HttpStatus.OK);
		return new ResponseEntity<List<ItineraryDTO>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//---
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ItineraryDTO>> getItineraryByUsernameUser(@RequestParam(required = true) String usernameUser){
		System.out.println("TEST: GET usernameUser");
		
		List<ItineraryDTO> result = itineraryService.findItineraryByUsernameUser(usernameUser);
		if(result != null) return new ResponseEntity<List<ItineraryDTO>>(result, HttpStatus.OK);
		return new ResponseEntity<List<ItineraryDTO>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
		
	//SEARCH
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ItineraryDTO>> searchUserByUsername(@RequestParam(required = true) String name){
		System.out.println("TEST: SEARCH");
		
		List<ItineraryDTO> result = itineraryService.searchItineraryByName(name);
		if(result != null) return new ResponseEntity<List<ItineraryDTO>>(result, HttpStatus.OK);
		return new ResponseEntity<List<ItineraryDTO>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ItineraryDTO> addUser(@RequestParam(required = true) String usernameUser,
												@RequestBody(required = true) ItineraryDTO itineraryDTO){
		System.out.println("TEST: ADD");
		
		/*
		Long idUser = userService.getIdByUsername(usernameUser);
		itineraryDTO.setIdUser(idUser);
		*/
		ItineraryDTO result = itineraryService.addItinerary(usernameUser, itineraryDTO);
		
		if(result != null) return new ResponseEntity<ItineraryDTO>(result, HttpStatus.OK);
		return new ResponseEntity<ItineraryDTO>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
		
	//PUTs

	
	//DELETEs
	
}
