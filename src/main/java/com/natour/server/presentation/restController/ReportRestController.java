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

import com.natour.server.application.dtos.request.SaveReportRequestDTO;
import com.natour.server.application.dtos.response.GetListReportResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.GetReportResponseDTO;
import com.natour.server.application.services.ReportService;
import com.natour.server.application.services.utils.ResultMessageUtils;



@RestController
@RequestMapping(value="/report")
public class ReportRestController {
	
	@Autowired
	private ReportService reportService;
	
	
	//GETs
	@RequestMapping(value="/get/{idReport}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetReportResponseDTO> getReportById(@PathVariable("idReport") long idReport){
		System.out.println("TEST: GET id");
		
		GetReportResponseDTO result = reportService.findReportById(idReport);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetReportResponseDTO>(result, resultHttpStatus);
	}
	
	
	@RequestMapping(value="/get/itinerary/{idItinerary}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetListReportResponseDTO> getReportByIdItinerary(@PathVariable("idItinerary") long idItinerary, @RequestParam(defaultValue = "0") Integer page){
		
		System.out.println("TEST: GET idItinerary");
		
		GetListReportResponseDTO result = reportService.findReportByIdItinerary(idItinerary, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetListReportResponseDTO>(result, resultHttpStatus);
	}
		

		
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> addReport(@RequestBody SaveReportRequestDTO saveReportRequest){
		System.out.println("TEST: ADD");
		
		ResultMessageDTO result = reportService.addReport(saveReportRequest);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
	}
		

		
	//DELETEs
	@RequestMapping(value="/delete/{idReport}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> deleteReportById(@PathVariable("idReport") long idReport){
		System.out.println("TEST: DELETE id");
		
		ResultMessageDTO result = reportService.removeReportById(idReport);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
	}
}
