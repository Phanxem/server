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

import com.natour.server.application.dtos.request.ReportRequestDTO;
import com.natour.server.application.dtos.response.ListReportResponseDTO;
import com.natour.server.application.dtos.response.MessageResponseDTO;
import com.natour.server.application.dtos.response.ReportResponseDTO;
import com.natour.server.application.services.ReportService;
import com.natour.server.application.services.ResultCodeUtils;



@RestController
@RequestMapping(value="/report")
public class ReportRestController {
	
	@Autowired
	private ReportService reportService;
	
	
	//GETs
	@RequestMapping(value="/get/{idReport}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ReportResponseDTO> getReportById(@PathVariable("idReport") long idReport){
		System.out.println("TEST: GET id");
		
		ReportResponseDTO result = reportService.findReportById(idReport);
		MessageResponseDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<ReportResponseDTO>(result, resultHttpStatus);
	}
	
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ListReportResponseDTO> getReportByIdItinerary(@RequestParam long idItinerary, @RequestParam int page){
		
		System.out.println("TEST: GET idItinerary");
		
		ListReportResponseDTO result = reportService.findReportByIdItinerary(idItinerary, page);
		MessageResponseDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<ListReportResponseDTO>(result, resultHttpStatus);
	}
		
	//SEARCH
		
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<MessageResponseDTO> addReport(@RequestParam long idUser,
											   		   @RequestBody ReportRequestDTO reportDTO){
		System.out.println("TEST: ADD");
		
		MessageResponseDTO result = reportService.addReport(idUser, reportDTO);
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(result.getCode());
		
		return new ResponseEntity<MessageResponseDTO>(result, resultHttpStatus);
	}
		
	//PUTs
		
	//DELETEs
	@RequestMapping(value="/delete/{idReport}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<MessageResponseDTO> deleteItineraryById(@PathVariable("idReport") long idReport){
		System.out.println("TEST: DELETE id");
		
		MessageResponseDTO result = reportService.removeReportById(idReport);
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(result.getCode());
		
		return new ResponseEntity<MessageResponseDTO>(result, resultHttpStatus);
	}
}
