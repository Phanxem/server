package com.natour.server.application.services;


import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.natour.server.application.dtos.request.SaveReportRequestDTO;
import com.natour.server.application.dtos.response.GetListReportResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.GetReportResponseDTO;
import com.natour.server.application.services.utils.DateUtils;
import com.natour.server.application.services.utils.ResultMessageUtils;
import com.natour.server.data.entities.rds.Itinerary;
import com.natour.server.data.entities.rds.Report;
import com.natour.server.data.entities.rds.User;
import com.natour.server.data.repository.rds.ItineraryRepository;
import com.natour.server.data.repository.rds.ReportRepository;
import com.natour.server.data.repository.rds.UserRepository;

@Service
public class ReportService {
	
	private final static int REPORT_PER_PAGE = 10;
	
	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ItineraryRepository itineraryRepository;
	
	
	//ADDs
	public ResultMessageDTO addReport(SaveReportRequestDTO reportRequestDTO) {
		
		if(!isValidDTO(reportRequestDTO)) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		Report report = toReportEntity(reportRequestDTO);
		
		if(report == null) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		Report result = reportRepository.save(report);
		
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}

		


	//UPDATEs
		
		
	//FINDs
	public GetReportResponseDTO findReportById(long id) {
		GetReportResponseDTO reportResponseDTO = new GetReportResponseDTO();
		
		Optional<Report> optionalReport = reportRepository.findById(id);
		if(!optionalReport.isPresent()) {
			reportResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return reportResponseDTO;
		}
		Report report = optionalReport.get();
		
		reportResponseDTO = toDto(report);
		
		return reportResponseDTO;
	}
	
	
	public GetListReportResponseDTO findReportByIdItinerary(Long idItinerary, int page) {
		GetListReportResponseDTO listReportResponseDTO = new GetListReportResponseDTO();
		
		if(idItinerary == null || idItinerary < 0) {
			listReportResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST);
			return listReportResponseDTO;
		}
		
		Optional<Itinerary> optionalItinerary = itineraryRepository.findById(idItinerary);
		if(!optionalItinerary.isPresent()) {
			listReportResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return listReportResponseDTO;
		}
		
		Pageable pageable = PageRequest.of(page, REPORT_PER_PAGE);
		List<Report> reports = reportRepository.findByItinerary_id(idItinerary, pageable);
		
		listReportResponseDTO = toDto(reports);
		return listReportResponseDTO;
	}
		
	//SEARCHs
		
		
	//REMOVEs
	public ResultMessageDTO removeReportById(long id) {
		Optional<Report> report = reportRepository.findById(id);
		if(!report.isPresent()) {
			return ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND;
		}
		
		reportRepository.delete(report.get());
	
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
	
	
	
	
	//MAPPERs
	public GetReportResponseDTO toDto(Report report) {
		if(report == null) return null;
		
		GetReportResponseDTO reportDTO = new GetReportResponseDTO();
		reportDTO.setId(report.getId());
		reportDTO.setName(report.getName());
		reportDTO.setDescription(report.getDescription());
		
		reportDTO.setIdUser(report.getUser().getId());
		
		reportDTO.setIdItinerary(report.getItinerary().getId());
		reportDTO.setNameItinerary(report.getItinerary().getName());
		
		//Date date = new Date(report.getDateOfInput().getTime());
		String string = DateUtils.toFullString(report.getDateOfInput());
		reportDTO.setDateOfInput(string);
		
		reportDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
		
		return reportDTO;
	}
	
	public GetListReportResponseDTO toDto(List<Report> reports){
		GetListReportResponseDTO listReportResponseDTO = new GetListReportResponseDTO();
		List<GetReportResponseDTO> reportsDTO = new LinkedList<GetReportResponseDTO>();
		
		if(reports == null) {
			listReportResponseDTO.setListReport(null);
			listReportResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
			return listReportResponseDTO;
		}
		
		for(Report report : reports) {
			reportsDTO.add(toDto(report));
		}
		
		listReportResponseDTO.setListReport(reportsDTO);
		listReportResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
		return listReportResponseDTO;
	}

	
	private Report toReportEntity(SaveReportRequestDTO reportRequestDTO) {

		Report report = new Report();
		report.setName(reportRequestDTO.getName());
		report.setDescription(reportRequestDTO.getDescription());
		
		Timestamp dateOfInput = null;
		try {
			dateOfInput = DateUtils.toTimestamp(reportRequestDTO.getDateOfInput());
		}
		catch (ParseException e) {
			return null;
		}
		
		report.setDateOfInput(dateOfInput);
		
		Optional<User> user = userRepository.findById(reportRequestDTO.getIdUser());
		if(!user.isPresent()) {
			return null;
		}
		report.setUser(user.get());
		
		Optional<Itinerary> itinerary = itineraryRepository.findById(reportRequestDTO.getIdItinerary());
		if(!itinerary.isPresent()) {
			return null;
		}
		report.setItinerary(itinerary.get());
		
		return report;
	}

	
	//VALIDATORs
	boolean isValidDTO(SaveReportRequestDTO reportRequestDTO) {
			
		if(reportRequestDTO == null) return false;
			
		if(reportRequestDTO.getName() == null ||
		   reportRequestDTO.getDateOfInput() == null ||
		   reportRequestDTO.getIdUser() == null ||
		   reportRequestDTO.getIdItinerary() == null)
		{
			return false;
		}
				
		return true;
	}

}
