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
import com.natour.server.application.dtos.response.ResultMessageDTO;
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
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<ReportResponseDTO>(result, resultHttpStatus);
	}
	
	
	@RequestMapping(value="/get/itinerary/{idItinerary}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ListReportResponseDTO> getReportByIdItinerary(@PathVariable("idItinerary") long idItinerary, @RequestParam(defaultValue = "0") Integer page){
		
		System.out.println("TEST: GET idItinerary");
		
		ListReportResponseDTO result = reportService.findReportByIdItinerary(idItinerary, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<ListReportResponseDTO>(result, resultHttpStatus);
	}
		
	//SEARCH
		
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> addReport(@RequestBody ReportRequestDTO reportDTO){
		System.out.println("TEST: ADD");
		
		ResultMessageDTO result = reportService.addReport(reportDTO);
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(result.getCode());
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
	}
		
	//PUTs
		
	//DELETEs
	@RequestMapping(value="/delete/{idReport}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> deleteItineraryById(@PathVariable("idReport") long idReport){
		System.out.println("TEST: DELETE id");
		
		ResultMessageDTO result = reportService.removeReportById(idReport);
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(result.getCode());
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
	}
}
