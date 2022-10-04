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

import com.natour.server.application.dtos.request.ItineraryRequestDTO;
import com.natour.server.application.dtos.response.ItineraryResponseDTO;
import com.natour.server.application.dtos.response.ListItineraryResponseDTO;
import com.natour.server.application.dtos.response.MessageResponseDTO;
import com.natour.server.application.services.ItineraryService;
import com.natour.server.application.services.ResultCodeUtils;



@RestController
@RequestMapping(value="/itinerary")
public class ItineraryRestController {

	@Autowired
	private ItineraryService itineraryService;
	
	
	//GETs
	@RequestMapping(value="/get/{idItinerary}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ItineraryResponseDTO> getItineraryById(@PathVariable("idItinerary") long idItinerary){
		System.out.println("TEST: GET id");
		
		ItineraryResponseDTO result = itineraryService.findItineraryById(idItinerary);
		MessageResponseDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<ItineraryResponseDTO>(result, resultHttpStatus);
		
	}
		
	//---
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ListItineraryResponseDTO> getItinerariesByIdUser(@RequestParam long idUser, @RequestParam int page){
		System.out.println("TEST: GET by idUser");
		
		ListItineraryResponseDTO result = itineraryService.findItineraryByIdUser(idUser, page);
		MessageResponseDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<ListItineraryResponseDTO>(result, resultHttpStatus);
	}
	
	//---

		
	//SEARCH
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ListItineraryResponseDTO> searchUserByUsername(@RequestParam String name, @RequestParam int page){
		System.out.println("TEST: SEARCH");
		
		ListItineraryResponseDTO result = itineraryService.searchItineraryByName(name, page);
		MessageResponseDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<ListItineraryResponseDTO>(result, resultHttpStatus);
	}
	
	
	
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<MessageResponseDTO> addUser(@ModelAttribute ItineraryRequestDTO itineraryRequestDTO){
		System.out.println("TEST: ADD");
		
		MessageResponseDTO result = itineraryService.addItinerary(itineraryRequestDTO);
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(result.getCode());
		
		return new ResponseEntity<MessageResponseDTO>(result, resultHttpStatus);
		
	}
	
	
		
	//PUTs
	@RequestMapping(value="/update/{idItinerary}", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<MessageResponseDTO> updateItinerary(@PathVariable("idItinerary") long idItinerary,
															  @RequestBody ItineraryRequestDTO itineraryDTO)
	{
		System.out.println("TEST: UPDATE ITINERARY");
		
		MessageResponseDTO result = itineraryService.updateItineraray(idItinerary, itineraryDTO);
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(result.getCode());
		
		return new ResponseEntity<MessageResponseDTO>(result, resultHttpStatus);
		
	}
	
	//DELETEs
	@RequestMapping(value="/delete/{idItinerary}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<MessageResponseDTO> deleteItineraryById(@PathVariable("idItinerary") long idItinerary){
		System.out.println("TEST: DELETE id");
		
		MessageResponseDTO result = itineraryService.removeItineraryById(idItinerary);
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(result.getCode());
		
		return new ResponseEntity<MessageResponseDTO>(result, resultHttpStatus);
		
	}
}
