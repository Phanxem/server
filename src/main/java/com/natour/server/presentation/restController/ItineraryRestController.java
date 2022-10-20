package com.natour.server.presentation.restController;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.natour.server.application.dtos.request.SaveItineraryRequestDTO;
import com.natour.server.application.dtos.response.GetItineraryResponseDTO;
import com.natour.server.application.dtos.response.GetListItineraryResponseDTO;
import com.natour.server.application.dtos.response.GetResourceResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.services.ItineraryService;
import com.natour.server.application.services.utils.ResultMessageUtils;



@RestController
@RequestMapping(value="/itinerary")
public class ItineraryRestController {

	@Autowired
	private ItineraryService itineraryService;
	
	
	//GETs
	@RequestMapping(value="/get/{idItinerary}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetItineraryResponseDTO> getItineraryById(@PathVariable("idItinerary") long idItinerary){
		System.out.println("TEST: GET id");
		
		GetItineraryResponseDTO result = itineraryService.findItineraryById(idItinerary);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetItineraryResponseDTO>(result, resultHttpStatus);
		
	}
	
	
	@RequestMapping(value="/get/{idItinerary}/gpx", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> getItineraryGpxById(@PathVariable("idItinerary") long idItinerary, HttpServletRequest request){
		System.out.println("TEST: GET IMAGE id");

		GetResourceResponseDTO result = itineraryService.findItineraryGpxById(idItinerary);

		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus;
		if(resultMessage.getCode() != 200) {
			resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
			return new ResponseEntity<Resource>((Resource) null, resultHttpStatus);
		}
		
		Resource resource = result.getResource();
		String contentType = null;
        /*
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		}
        catch (IOException e) {
			//TODO
			return new ResponseEntity<Resource>((Resource) null, HttpStatus.NOT_FOUND);
		}*/
		
        if(contentType == null) contentType = "application/octet-stream";
        
		return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
	}
	
		
	//---
	
	@RequestMapping(value="/get/random", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetListItineraryResponseDTO> getRandomItineraries(){
		System.out.println("TEST: GET random");
		
		GetListItineraryResponseDTO result = itineraryService.findRandomItineraries();
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetListItineraryResponseDTO>(result, resultHttpStatus);
	}
	
	@RequestMapping(value="/get/user/{idUser}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetListItineraryResponseDTO> getItinerariesByIdUser(@PathVariable("idUser") long idUser, @RequestParam int page){
		System.out.println("TEST: GET by idUser");
		
		GetListItineraryResponseDTO result = itineraryService.findItineraryByIdUser(idUser, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetListItineraryResponseDTO>(result, resultHttpStatus);
	}
	
	//---

		
	//SEARCH
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetListItineraryResponseDTO> searchItineraryByName(@RequestParam String name, @RequestParam(defaultValue = "0") Integer page){
		System.out.println("TEST: SEARCH");
		
		GetListItineraryResponseDTO result = itineraryService.searchItineraryByName(name, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetListItineraryResponseDTO>(result, resultHttpStatus);
	}
	
	
	
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> addItinerary(@ModelAttribute SaveItineraryRequestDTO itineraryRequestDTO){
		System.out.println("TEST: ADD");
		
		ResultMessageDTO result = itineraryService.addItinerary(itineraryRequestDTO);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
		
	}
	
	
		
	//PUTs
	@RequestMapping(value="/update/{idItinerary}", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> updateItinerary(@PathVariable("idItinerary") long idItinerary,
															@ModelAttribute SaveItineraryRequestDTO itineraryDTO)
	{
		System.out.println("TEST: UPDATE ITINERARY");
		
		ResultMessageDTO result = itineraryService.updateItineraray(idItinerary, itineraryDTO);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
		
	}
	
	//DELETEs
	@RequestMapping(value="/delete/{idItinerary}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> deleteItineraryById(@PathVariable("idItinerary") long idItinerary){
		System.out.println("TEST: DELETE id");
		
		ResultMessageDTO result = itineraryService.removeItineraryById(idItinerary);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
		
	}
}
