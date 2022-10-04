package com.natour.server.application.services;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.natour.server.application.dtos.request.ReportRequestDTO;
import com.natour.server.application.dtos.response.ListReportResponseDTO;
import com.natour.server.application.dtos.response.MessageResponseDTO;
import com.natour.server.application.dtos.response.ReportResponseDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.ItineraryNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.ReportDTOInvalidException;
import com.natour.server.application.exceptionHandler.serverExceptions.ItineraryIdNullException;
import com.natour.server.application.exceptionHandler.serverExceptions.ReportNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserUsernameNullException;
import com.natour.server.data.entities.Itinerary;
import com.natour.server.data.entities.Report;
import com.natour.server.data.entities.User;
import com.natour.server.data.repository.ItineraryRepository;
import com.natour.server.data.repository.ReportRepository;
import com.natour.server.data.repository.UserRepository;

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
	public MessageResponseDTO addReport(long idUser, ReportRequestDTO reportRequestDTO) {
		if(idUser < 0) {
			//TODO
			throw new UserUsernameNullException();
		}
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(optionalUser.isEmpty()) {
			//TODO
		}
		
		if(!isValidDTO(reportRequestDTO)) {
			//TODO
			throw new ReportDTOInvalidException();
		}
		
		reportRequestDTO.setIdUser(idUser);
		Report report = toReportEntity(reportRequestDTO);
		
		Report result = reportRepository.save(report);
		
		return new MessageResponseDTO();
	}

		


	//UPDATEs
		
		
	//FINDs
	public ReportResponseDTO findReportById(long id) {
		Optional<Report> optionalReport = reportRepository.findById(id);
		if(!optionalReport.isPresent()) {
			//TODO
			throw new ReportNotFoundException();
		}
		Report report = optionalReport.get();
		
		ReportResponseDTO reportResponseDTO = toReportResponseDTO(report);
		
		return reportResponseDTO;
	}
	
	
	public ListReportResponseDTO findReportByIdItinerary(Long idItinerary, int page) {
		if(idItinerary == null || idItinerary < 0) {
			//TODO
			throw new ItineraryIdNullException();
		}
		
		Optional<Itinerary> optionalItinerary = itineraryRepository.findById(idItinerary);
		if(!optionalItinerary.isPresent()) {
			//TODO
			throw new ItineraryNotFoundException();
		}
		
		Pageable pageable = PageRequest.of(page, REPORT_PER_PAGE);
		List<Report> reports = reportRepository.findByItinerary_id(idItinerary, pageable);
		ListReportResponseDTO listReportResponseDTO = toListReportResponseDTO(reports);
		return listReportResponseDTO;
	}
		
	//SEARCHs
		
		
	//REMOVEs
	public MessageResponseDTO removeReportById(long id) {
		Optional<Report> report = reportRepository.findById(id);
		if(!report.isPresent()) {
			//TODO
			throw new ReportNotFoundException();
		}
		
		reportRepository.delete(report.get());
	
		return new MessageResponseDTO();
	}
	
	
	
	
	//MAPPERs
	//Entities -> DTOs
	public ReportResponseDTO toReportResponseDTO(Report report) {
		if(report == null) return null;
		
		ReportResponseDTO reportDTO = new ReportResponseDTO();
		reportDTO.setId(report.getId());
		reportDTO.setName(report.getName());
		reportDTO.setDescription(report.getDescription());
		
		reportDTO.setIdUser(report.getUser().getId());
		reportDTO.setIdItinerary(report.getItinerary().getId());
		
		Date date = new Date(report.getDateOfInput().getTime());
		reportDTO.setDateOfInput(date);
		
		reportDTO.setResultMessage(new MessageResponseDTO());
		
		return reportDTO;
	}
	
	public ListReportResponseDTO toListReportResponseDTO(List<Report> reports){
		ListReportResponseDTO listReportResponseDTO = new ListReportResponseDTO();
		List<ReportResponseDTO> reportsDTO = new LinkedList<ReportResponseDTO>();
		
		if(reports == null) {
			listReportResponseDTO.setListReport(null);
			listReportResponseDTO.setResultMessage(new MessageResponseDTO());
			return listReportResponseDTO;
		}
		
		for(Report report : reports) {
			reportsDTO.add(toReportResponseDTO(report));
		}
		
		listReportResponseDTO.setListReport(reportsDTO);
		listReportResponseDTO.setResultMessage(new MessageResponseDTO());
		return listReportResponseDTO;
	}

	
	//DTOs -> Entities
	private Report toReportEntity(ReportRequestDTO reportRequestDTO) {
		
		Report report = new Report();
		report.setId(reportRequestDTO.getId());
		report.setName(reportRequestDTO.getName());
		report.setDescription(reportRequestDTO.getDescription());
		
		Optional<User> user = userRepository.findById(reportRequestDTO.getIdUser());
		if(!user.isPresent()) throw new UserNotFoundException();
		report.setUser(user.get());
		
		Optional<Itinerary> itinerary = itineraryRepository.findById(reportRequestDTO.getIdItinerary());
		if(!itinerary.isPresent()) throw new ItineraryNotFoundException();
		report.setItinerary(itinerary.get());
		
		return report;
	}

	
	//VALIDATORs
	boolean isValidDTO(ReportRequestDTO reportRequestDTO) {
			
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
