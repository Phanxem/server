package com.natour.server.presentation.restController;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.natour.server.application.dtos.MessageDTO;
import com.natour.server.application.dtos.request.ItineraryRequestDTO;
import com.natour.server.application.dtos.response.ItineraryResponseDTO;
import com.natour.server.application.services.ItineraryService;



@RestController
@RequestMapping(value="/itinerary")
public class ItineraryRestController {

	@Autowired
	private ItineraryService itineraryService;
	
	
	//GETs
	@RequestMapping(value="/get/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ItineraryResponseDTO> getItineraryById(@PathVariable("id") long id){
		System.out.println("TEST: GET id");
		
		ItineraryResponseDTO result = itineraryService.findItineraryById(id);
		return new ResponseEntity<ItineraryResponseDTO>(result, HttpStatus.OK);
		
	}
		
	//---
	
	@RequestMapping(value="/get/user/{idUser}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ItineraryResponseDTO>> getItinerariesByIdUser(@PathVariable("idUser") Long idUser){
		System.out.println("TEST: GET idUser");
		
		List<ItineraryResponseDTO> result = itineraryService.findItineraryByIdUser(idUser);
		return new ResponseEntity<List<ItineraryResponseDTO>>(result, HttpStatus.OK);
		
	}
	
	//---
	
	@RequestMapping(value="/get/user", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ItineraryResponseDTO>> getItineraryByUsernameUser(@RequestParam String username){
		System.out.println("TEST: GET usernameUser");
		
		List<ItineraryResponseDTO> result = itineraryService.findItineraryByUsernameUser(username);
		return new ResponseEntity<List<ItineraryResponseDTO>>(result, HttpStatus.OK);
		
	}
	
	
		
	//SEARCH
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ItineraryResponseDTO>> searchUserByUsername(@RequestParam String name){
		System.out.println("TEST: SEARCH");
		
		List<ItineraryResponseDTO> result = itineraryService.searchItineraryByName(name);
		return new ResponseEntity<List<ItineraryResponseDTO>>(result, HttpStatus.OK);
		
	}
	
	
	
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<MessageDTO> addUser(@RequestParam String usernameUser,
											  @ModelAttribute ItineraryRequestDTO itineraryRequestDTO){
		System.out.println("TEST: ADD");
		
		/*
		Long idUser = userService.getIdByUsername(usernameUser);
		itineraryDTO.setIdUser(idUser);
		*/
		
		MessageDTO result = itineraryService.addItinerary(usernameUser, itineraryRequestDTO);
		
		return new ResponseEntity<MessageDTO>(result, HttpStatus.OK);
		
	}
	
	
		
	//PUTs
	@RequestMapping(value="/update/{id}", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<MessageDTO> updateItinerary(@PathVariable("id") long id,
														@RequestBody ItineraryRequestDTO itineraryDTO)
	{
		System.out.println("TEST: UPDATE ITINERARY");
		
		MessageDTO result = itineraryService.updateItineraray(id, itineraryDTO);
		
		return new ResponseEntity<MessageDTO>(result, HttpStatus.OK);
		
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
