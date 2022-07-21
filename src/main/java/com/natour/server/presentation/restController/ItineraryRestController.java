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
import com.natour.server.application.dtos.MessageDTO;
import com.natour.server.application.services.ItineraryService;



@RestController
@RequestMapping(value="/itinerary")
public class ItineraryRestController {

	@Autowired
	private ItineraryService itineraryService;
	
	//GETs
	@RequestMapping(value="/get/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ItineraryDTO> getItineraryById(@PathVariable("id") long id){
		System.out.println("TEST: GET id");
		
		ItineraryDTO result = itineraryService.findItineraryById(id);
		return new ResponseEntity<ItineraryDTO>(result, HttpStatus.OK);
		
	}
		
	//---
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ItineraryDTO>> getItineraryByIdUser(@RequestParam Long idUser){
		System.out.println("TEST: GET idUser");
		
		List<ItineraryDTO> result = itineraryService.findItineraryByIdUser(idUser);
		return new ResponseEntity<List<ItineraryDTO>>(result, HttpStatus.OK);
		
	}
	
	//---
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ItineraryDTO>> getItineraryByUsernameUser(@RequestParam String usernameUser){
		System.out.println("TEST: GET usernameUser");
		
		List<ItineraryDTO> result = itineraryService.findItineraryByUsernameUser(usernameUser);
		return new ResponseEntity<List<ItineraryDTO>>(result, HttpStatus.OK);
		
	}
	
	
		
	//SEARCH
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ItineraryDTO>> searchUserByUsername(@RequestParam String name){
		System.out.println("TEST: SEARCH");
		
		List<ItineraryDTO> result = itineraryService.searchItineraryByName(name);
		return new ResponseEntity<List<ItineraryDTO>>(result, HttpStatus.OK);
		
	}
	
	
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ItineraryDTO> addUser(@RequestParam String usernameUser,
												@RequestBody ItineraryDTO itineraryDTO){
		System.out.println("TEST: ADD");
		
		/*
		Long idUser = userService.getIdByUsername(usernameUser);
		itineraryDTO.setIdUser(idUser);
		*/
		ItineraryDTO result = itineraryService.addItinerary(usernameUser, itineraryDTO);
		
		return new ResponseEntity<ItineraryDTO>(result, HttpStatus.OK);
		
	}
	
	
		
	//PUTs
	@RequestMapping(value="/update/{id}", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ItineraryDTO> updateItinerary(@PathVariable("id") long id,
														@RequestBody ItineraryDTO itineraryDTO)
	{
		System.out.println("TEST: UPDATE ITINERARY");
		
		ItineraryDTO result = itineraryService.updateItineraray(id, itineraryDTO);
		
		return new ResponseEntity<ItineraryDTO>(result, HttpStatus.OK);
		
	}
	
	//DELETEs
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<MessageDTO> deleteItineraryById(@PathVariable("id") long id){
		System.out.println("TEST: DELETE id");
		
		MessageDTO result = itineraryService.removeItineraryById(id);
		return new ResponseEntity<MessageDTO>(result, HttpStatus.OK);
		
	}
}
