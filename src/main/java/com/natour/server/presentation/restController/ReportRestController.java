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
import com.natour.server.application.dtos.ReportDTO;
import com.natour.server.application.services.ReportService;
import com.natour.server.application.services.UserService;



@RestController
@RequestMapping(value="/report")
public class ReportRestController {
	
	@Autowired
	private ReportService reportService;
	@Autowired
	private UserService userService;
	
	//GETs
	@RequestMapping(value="/get/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ReportDTO> getUserById(@PathVariable("id") long id){
		System.out.println("TEST: GET id");
		
		ReportDTO result = reportService.findReportById(id);
		if(result != null) return new ResponseEntity<ReportDTO>(result, HttpStatus.OK);
		return new ResponseEntity<ReportDTO>(result, HttpStatus.NOT_FOUND);
	}
	
	//---
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<ReportDTO>> getUserByIdItinerary(@RequestParam(required = true) long idItinerary){
		System.out.println("TEST: GET idItinerary");
		
		List<ReportDTO> result = reportService.findReportByIdItinerary(idItinerary);
		if(result != null) return new ResponseEntity<List<ReportDTO>>(result, HttpStatus.OK);
		return new ResponseEntity<List<ReportDTO>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
		
	//SEARCH
		
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ReportDTO> addReport(@RequestParam(required = true) String usernameUser,
												@RequestBody(required = true) ReportDTO reportDTO){
		System.out.println("TEST: ADD");
		
		
		ReportDTO result = reportService.addReport(usernameUser, reportDTO);
		
		if(result != null) return new ResponseEntity<ReportDTO>(result, HttpStatus.OK);
		return new ResponseEntity<ReportDTO>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
		
	//PUTs
		
	//DELETEs

}
