package com.natour.server.application.services;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natour.server.application.dtos.ReportDTO;
import com.natour.server.data.entities.Itinerary;
import com.natour.server.data.entities.Report;
import com.natour.server.data.entities.User;
import com.natour.server.data.repository.ItineraryRepository;
import com.natour.server.data.repository.ReportRepository;
import com.natour.server.data.repository.UserRepository;

@Service
public class ReportService {

	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ItineraryRepository itineraryRepository;
	
	/*
	@Autowired
	private UserService userService;
*/
	
	
	//ADDs
	public ReportDTO addReport(String usernameUser, ReportDTO reportDTO) {
		
		Long idUser = userRepository.findIdByUsername(usernameUser);
		//TODO error handling
		if(idUser == null) return null;
		
		reportDTO.setIdUser(idUser);
		
		Report report = toReportEntity(reportDTO);
		
		Report result = reportRepository.save(report);
		
		return toReportDTO(result);
	}

		


	//UPDATEs
		
		
	//FINDs
	public ReportDTO findReportById(long id) {
		Optional<Report> report = reportRepository.findById(id);
		if(report.isPresent()) return toReportDTO(report.get());
		return null;
	}
	
	public List<ReportDTO> findReportByIdItinerary(long idItinerary) {
		List<Report> reports = reportRepository.findByItinerary_id(idItinerary);
		List<ReportDTO> reportsDTO = toListReportDTO(reports);
		return reportsDTO;
	}
		
	//SEARCHs
		
		
	//REMOVEs
	
	
	
	
	//MAPPERs
	
	public ReportDTO toReportDTO(Report report) {
		
		if(report == null) return null;
		
		ReportDTO reportDTO = new ReportDTO();
		reportDTO.setId(report.getId());
		reportDTO.setName(report.getName());
		reportDTO.setDescription(report.getDescription());
		
		reportDTO.setIdUser(report.getUser().getId());
		reportDTO.setIdItinerary(report.getItinerary().getId());
		
		Date date = new Date(report.getDateOfInput().getTime());
		reportDTO.setDateOfInput(date);
		
		return reportDTO;
	}
	
	public List<ReportDTO> toListReportDTO(List<Report> reports){
		if(reports == null) return null;
		
		List<ReportDTO> reportsDTO = new LinkedList<ReportDTO>();
		for(Report report : reports) {
			reportsDTO.add(toReportDTO(report));
		}
		return reportsDTO;
	}

	private Report toReportEntity(ReportDTO reportDTO) {
		if(reportDTO == null) return null;
		
		Report report = new Report();
		report.setId(reportDTO.getId());
		report.setName(reportDTO.getName());
		report.setDescription(reportDTO.getDescription());
		
		User user = userRepository.getReferenceById(reportDTO.getIdUser());
		report.setUser(user);
		
		Itinerary itinerary = itineraryRepository.getReferenceById(reportDTO.getIdItinerary());
		report.setItinerary(itinerary);
		
		return report;
	}

	
}
