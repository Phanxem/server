package com.natour.server.application.services;


import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natour.server.application.dtos.MessageDTO;
import com.natour.server.application.dtos.ReportDTO;
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

	private final static long SUCCESSFUL_REMOVAL_CODE = 101;
	private final static String SUCCESSFUL_REMOVAL_MESSAGE = "Segnalazione rimossa con successo.";
	
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
		if(usernameUser == null) throw new UserUsernameNullException();
		
		if(!isValidDTO(reportDTO)) throw new ReportDTOInvalidException();
		
		Long idUser = userRepository.findIdByUsername(usernameUser);
		if(idUser == null) throw new UserNotFoundException();
		
		reportDTO.setIdUser(idUser);
		Report report = toReportEntity(reportDTO);
		
		Report result = reportRepository.save(report);
		
		return toReportDTO(result);
	}

		


	//UPDATEs
		
		
	//FINDs
	public ReportDTO findReportById(long id) {
		Optional<Report> report = reportRepository.findById(id);
		if(!report.isPresent()) throw new ReportNotFoundException();
		return toReportDTO(report.get());
	}
	
	public List<ReportDTO> findReportByIdItinerary(Long idItinerary) {
		if(idItinerary == null) throw new ItineraryIdNullException();
		
		Optional<Itinerary> itinerary = itineraryRepository.findById(idItinerary);
		if(!itinerary.isPresent()) throw new ItineraryNotFoundException();
		
		//List<Report> reports = reportRepository.findByItinerary_id(itinerary.get().getId());
		List<Report> reports = reportRepository.findByItinerary(itinerary.get());
		List<ReportDTO> reportsDTO = toListReportDTO(reports);
		return reportsDTO;
	}
		
	//SEARCHs
		
		
	//REMOVEs
	public MessageDTO removeReportById(long id) {
		Optional<Report> report = reportRepository.findById(id);
		if(!report.isPresent()) throw new ReportNotFoundException();
		
		reportRepository.delete(report.get());
		
		MessageDTO message = new MessageDTO(SUCCESSFUL_REMOVAL_CODE, SUCCESSFUL_REMOVAL_MESSAGE);
		return message;
	}
	
	
	
	//MAPPERs
	//Entities -> DTOs
	
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
		
		if(reports.isEmpty()) return reportsDTO;
		
		for(Report report : reports) {
			reportsDTO.add(toReportDTO(report));
		}
		return reportsDTO;
	}

	//DTOs -> Entities
	
	private Report toReportEntity(ReportDTO reportDTO) {
		
		Report report = new Report();
		report.setId(reportDTO.getId());
		report.setName(reportDTO.getName());
		report.setDescription(reportDTO.getDescription());
		
		Optional<User> user = userRepository.findById(reportDTO.getIdUser());
		if(!user.isPresent()) throw new UserNotFoundException();
		report.setUser(user.get());
		
		Optional<Itinerary> itinerary = itineraryRepository.findById(reportDTO.getIdItinerary());
		if(!itinerary.isPresent()) throw new ItineraryNotFoundException();
		report.setItinerary(itinerary.get());
		
		return report;
	}

	//VALIDATORs
	
	boolean isValidDTO(ReportDTO reportDTO) {
			
		if(reportDTO == null) return false;
			
		if(reportDTO.getName() == null ||
		   reportDTO.getDateOfInput() == null ||
		   reportDTO.getIdUser() == null ||
		   reportDTO.getIdItinerary() == null)
		{
			return false;
		}
				
		return true;
	}




	
}
