package com.natour.server.application.services;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.natour.server.application.dtos.request.ItineraryRequestDTO;
import com.natour.server.application.dtos.response.ItineraryResponseDTO;
import com.natour.server.application.dtos.response.ListItineraryResponseDTO;
import com.natour.server.application.dtos.response.ResourceResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.UserIdNullException;
import com.natour.server.application.exceptionHandler.serverExceptions.FileConvertionFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.ItineraryDTOInvalidException;
import com.natour.server.application.exceptionHandler.serverExceptions.ItineraryGPXFileSaveFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.ItineraryGPXFileUpdateFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.ItineraryNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserUsernameNullException;
import com.natour.server.data.entities.rds.Itinerary;
import com.natour.server.data.entities.rds.User;
import com.natour.server.data.repository.rds.ItineraryRepository;
import com.natour.server.data.repository.rds.UserRepository;
import com.natour.server.data.repository.s3.FileSystemRepository;

import io.jenetics.jpx.GPX;

@Service
public class ItineraryService {

	private final static int ITINERARY_PER_PAGE = 20;
	
	@Autowired
	private ItineraryRepository itineraryRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FileSystemRepository fileSystemRepository;
	
	//ADDs
	public ResultMessageDTO addItinerary(ItineraryRequestDTO itineraryRequestDTO) {
		
		
		
		if(!isValidDTO(itineraryRequestDTO)) {
			//TODO
			throw new ItineraryDTOInvalidException();
		}
		
		Itinerary itinerary = toItineraryEntity(itineraryRequestDTO);
		itinerary = itineraryRepository.save(itinerary);

		
		String gpxName = "gpx-" + itinerary.getId();
		MultipartFile gpx = itineraryRequestDTO.getGpx();
		String gpxUrl;	
		try {
			gpxUrl = fileSystemRepository.save(gpxName, gpx.getBytes());
		}
		catch (IOException e) {
			//TODO
			itineraryRepository.delete(itinerary);
			throw new ItineraryGPXFileSaveFailureException(e);
		}
		itinerary.setGpxURL(gpxUrl);
		
		Itinerary result = itineraryRepository.save(itinerary);
		
		return new ResultMessageDTO();
	}

	
	//UPDATEs
	public ResultMessageDTO updateItineraray(long idItinerary, ItineraryRequestDTO itineraryRequestDTO) {
		
		Optional<Itinerary> optionalItinerary = itineraryRepository.findById(idItinerary);
		if(!optionalItinerary.isPresent()) {
			//TODO
			throw new ItineraryNotFoundException();
		}
		Itinerary oldItinerary = optionalItinerary.get();
		User user = oldItinerary.getUser();
				
		itineraryRequestDTO.setIdUser(user.getId());
		
		if(!isValidDTO(itineraryRequestDTO)) {
			//TODO
			throw new ItineraryDTOInvalidException();
		}
		
	
		MultipartFile gpx = itineraryRequestDTO.getGpx();		
		String gpxUrl;	
		try {
			//TODO testare
			fileSystemRepository.delete(oldItinerary.getGpxURL());
			gpxUrl = fileSystemRepository.save("gpx-" + idItinerary, gpx.getBytes());
		}
		catch (IOException e) {
			//TODO
			throw new ItineraryGPXFileSaveFailureException(e);
		}
		
		Itinerary itinerary = toItineraryEntity(itineraryRequestDTO);
		itinerary.setId(idItinerary);
		itinerary.setGpxURL(gpxUrl);
		
		Itinerary result = itineraryRepository.save(itinerary);
		
		return new ResultMessageDTO();
	}
	
	
	
	
	//FINDs
	public ItineraryResponseDTO findItineraryById(long id) {
		Optional<Itinerary> optionalItinerary = itineraryRepository.findById(id);
		if(!optionalItinerary.isPresent()) {
			//TODO
			throw new ItineraryNotFoundException(); 
		}
		Itinerary itinerary = optionalItinerary.get();
		
		ItineraryResponseDTO itineraryResponseDTO = toItineraryResponseDTO(itinerary);
		
		return itineraryResponseDTO;
	}
	
	public ResourceResponseDTO findItineraryGpxById(long idItinerary) {
		Optional<Itinerary> optionalItinerary = itineraryRepository.findById(idItinerary);
		if(!optionalItinerary.isPresent()) {
			//TODO
			throw new UserNotFoundException();
		}
		Itinerary itinerary = optionalItinerary.get();
		
		ResourceResponseDTO gpxResponseDTO = new ResourceResponseDTO();
		if(itinerary.getGpxURL() != null) {
			FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(itinerary.getGpxURL());
			if(fileSystemResource != null) {
				gpxResponseDTO.setResource(fileSystemResource);
				gpxResponseDTO.setResultMessage(new ResultMessageDTO());
				
				return gpxResponseDTO;
			}
			gpxResponseDTO.setResultMessage(new ResultMessageDTO(-100, "errore1"));
			return gpxResponseDTO;
		}
		gpxResponseDTO.setResultMessage(new ResultMessageDTO(-100, "errore1"));
		return gpxResponseDTO;
	}
	
	
	public ListItineraryResponseDTO findItineraryByIdUser(Long idUser, int page) {
		if(idUser == null || idUser < 0) {
			//TODO
			throw new UserIdNullException();
		}
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(!optionalUser.isPresent()) {
			//TODO
			throw new UserNotFoundException();
		}
		
		Pageable pageable = PageRequest.of(page, ITINERARY_PER_PAGE);
		List<Itinerary> itineraries = itineraryRepository.findByUser_id(idUser, pageable);
		ListItineraryResponseDTO itinerariesDTO = toListItineraryResponseDTO(itineraries);
		
		return itinerariesDTO;
	}
		
	public ListItineraryResponseDTO findRandomItineraries() {
		
		Pageable pageable = PageRequest.of(0, ITINERARY_PER_PAGE);
		List<Itinerary> itineraries = itineraryRepository.findRandom(pageable);
		ListItineraryResponseDTO itinerariesDTO = toListItineraryResponseDTO(itineraries);
		
		return itinerariesDTO;
	}

	
	//SEARCHs
	public ListItineraryResponseDTO searchItineraryByName(String name, int page) {
		Pageable pageable = PageRequest.of(page, ITINERARY_PER_PAGE);
		List<Itinerary> itineraries = itineraryRepository.findByNameContaining(name,pageable);
		
		System.out.println("size: " + itineraries.size());
		
		ListItineraryResponseDTO itinerariesDTO = toListItineraryResponseDTO(itineraries);

		return itinerariesDTO;
	}
		
		
	//REMOVEs
	public ResultMessageDTO removeItineraryById(long id) {
		Optional<Itinerary> optionalItinerary = itineraryRepository.findById(id);
		if(!optionalItinerary.isPresent()) {
			//TODO
			throw new ItineraryNotFoundException();
		}
		Itinerary itinerary = optionalItinerary.get();
		String gpxUrl = itinerary.getGpxURL();
		
		itineraryRepository.delete(itinerary);
		try {
			fileSystemRepository.delete(gpxUrl);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ResultMessageDTO();
	}
	
	//----------------------------------------------------------------------
	
	//MAPPER

	public Itinerary toItineraryEntity(ItineraryRequestDTO itineraryDTO) {
		
		Itinerary itinerary = new Itinerary();
		itinerary.setName(itineraryDTO.getName());
		itinerary.setDescription(itineraryDTO.getDescription());
		itinerary.setDifficulty(itineraryDTO.getDifficulty());
		itinerary.setDuration(itineraryDTO.getDuration());
		itinerary.setLenght(itineraryDTO.getLenght());
		
		Optional<User> user = userRepository.findById(itineraryDTO.getIdUser());
		if(!user.isPresent()) throw new UserNotFoundException();
		itinerary.setUser(user.get());
		
		itinerary.setGpxURL("");
		
		return itinerary;
	}
	

	
	
	
	//----
	
	public ItineraryResponseDTO toItineraryResponseDTO(Itinerary itinerary) {
		
		if(itinerary == null) return null;
		
		ItineraryResponseDTO dto = new ItineraryResponseDTO();
		
		dto.setId(itinerary.getId());
		dto.setName(itinerary.getName());
		dto.setDescription(itinerary.getDescription());
		dto.setDifficulty(itinerary.getDifficulty());
		dto.setDuration(itinerary.getDuration());
		dto.setLenght(itinerary.getLenght());
		
		dto.setIdUser(itinerary.getUser().getId());
		
		dto.setResultMessage(new ResultMessageDTO());
		
		return dto;
	}
	
	
	public ListItineraryResponseDTO toListItineraryResponseDTO(List<Itinerary> itineraries){
		
		ListItineraryResponseDTO listItineraryResponseDTO = new ListItineraryResponseDTO();
		List<ItineraryResponseDTO> itinerariesDTO = new LinkedList<ItineraryResponseDTO>();
		
		
		if(itineraries == null) {
			listItineraryResponseDTO.setListItinerary(null);
			listItineraryResponseDTO.setResultMessage(new ResultMessageDTO());
			return listItineraryResponseDTO;
		}
		
		
		for(Itinerary itinerary : itineraries) {
			itinerariesDTO.add(toItineraryResponseDTO(itinerary));
		}
		
		listItineraryResponseDTO.setListItinerary(itinerariesDTO);
		listItineraryResponseDTO.setResultMessage(new ResultMessageDTO());
		return listItineraryResponseDTO;
	}
	
	
	
	//VALIDATORs
	
	public boolean isValidDTO(ItineraryRequestDTO itineraryDTO) {
		
		if(itineraryDTO == null) return false;
				
		if(itineraryDTO.getName() == null) System.out.println("errore nome");
		
		if(itineraryDTO.getName() == null ||
		   itineraryDTO.getDifficulty() == null ||
		   itineraryDTO.getDifficulty() < 0 ||
		   itineraryDTO.getDifficulty() > 2 ||
		   itineraryDTO.getLenght() == null ||
		   itineraryDTO.getDuration() == null ||
		   itineraryDTO.getGpx() == null ||
		   itineraryDTO.getIdUser() == null )
		{
			return false;
		}
		
		try {
			if(itineraryDTO.getGpx().getBytes().length == 0) return false;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		

		if(itineraryDTO.getIdUser() < 0) return false;
			
		Optional<User> optionalUser = userRepository.findById(itineraryDTO.getIdUser());
		if(optionalUser.isEmpty()) return false;
		
		return true;
	}


	//CONVERTERs
	
	public byte[] toArrayByte(GPX gpx, String fileName) {
		File file = new File(fileName + ".gpx");
		byte[] gpxByte;
		
		//TODO DA TESTARE (DELICATO)
		try {
			GPX.writer().write(gpx, file);
			gpxByte = Files.readAllBytes(file.toPath());
		}
		catch (IOException e) {
			throw new FileConvertionFailureException();
		}
		
		return gpxByte;
	}


	


	

	
	
	
	
	
}
