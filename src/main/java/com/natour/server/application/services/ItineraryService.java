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

import com.natour.server.application.dtos.request.SaveItineraryRequestDTO;
import com.natour.server.application.dtos.response.GetItineraryResponseDTO;
import com.natour.server.application.dtos.response.GetListItineraryResponseDTO;
import com.natour.server.application.dtos.response.GetResourceResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.StringResponseDTO;
import com.natour.server.application.services.utils.ResultMessageUtils;
import com.natour.server.data.dao.interfaces.GpxDAO;
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
	//@Autowired
	//private FileSystemRepository fileSystemRepository;
	
	@Autowired
	private GpxDAO gpxDAO;
	
	//ADDs
	public ResultMessageDTO addItinerary(SaveItineraryRequestDTO itineraryRequestDTO) {
		
		if(!isValidDTO(itineraryRequestDTO)) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		Itinerary itinerary = toItineraryEntity(itineraryRequestDTO);
		if(itinerary == null) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		
		MultipartFile gpx = itineraryRequestDTO.getGpx();
		
		byte[] gpxBytes = null;
		try {
			gpxBytes = gpx.getBytes();
		}
		catch (IOException e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		
		itinerary = itineraryRepository.save(itinerary);
		
		StringResponseDTO stringResponseDTO = gpxDAO.put(String.valueOf(itinerary.getId()), gpxBytes);
		ResultMessageDTO resultMessageDTO = stringResponseDTO.getResultMessage();
		if(!ResultMessageUtils.isSuccess(resultMessageDTO)) {
			itineraryRepository.delete(itinerary);
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		
		String gpxKey = stringResponseDTO.getString();
		
		/*
		String gpxUrl;	
		try {
			gpxUrl = fileSystemRepository.save(gpxName, gpx.getBytes());
		}
		catch (IOException e) {
			//TODO
			itineraryRepository.delete(itinerary);
			throw new ItineraryGPXFileSaveFailureException(e);
		}
		*/
		
		
		itinerary.setGpxKey(gpxKey);
		itinerary = itineraryRepository.save(itinerary);
		
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}

	
	//UPDATEs
	public ResultMessageDTO updateItineraray(long idItinerary, SaveItineraryRequestDTO itineraryRequestDTO) {
		
		Optional<Itinerary> optionalItinerary = itineraryRepository.findById(idItinerary);
		if(!optionalItinerary.isPresent()) {
			return ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND;
		}
		Itinerary oldItinerary = optionalItinerary.get();
		User user = oldItinerary.getUser();
				
		itineraryRequestDTO.setIdUser(user.getId());
		
		if(!isValidDTO(itineraryRequestDTO)) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
	
		MultipartFile gpx = itineraryRequestDTO.getGpx();	
		
		byte[] gpxBytes = null;
		try {
			gpxBytes = gpx.getBytes();
		}
		catch (IOException e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		StringResponseDTO stringResponseDTO = gpxDAO.put(String.valueOf(idItinerary), gpxBytes);
		ResultMessageDTO resultMessageDTO = stringResponseDTO.getResultMessage();
		if(!ResultMessageUtils.isSuccess(resultMessageDTO)) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		String gpxUrl = stringResponseDTO.getString();
		
		/*
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
		*/
		
		
		
		
		Itinerary itinerary = toItineraryEntity(itineraryRequestDTO);
		if(itinerary == null) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		itinerary.setId(idItinerary);
		itinerary.setGpxKey(gpxUrl);
		
		gpxDAO.delete(oldItinerary.getGpxKey());
		
		itinerary = itineraryRepository.save(itinerary);
		
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
	
	
	
	
	//FINDs
	public GetItineraryResponseDTO findItineraryById(long id) {
		GetItineraryResponseDTO itineraryResponseDTO = new GetItineraryResponseDTO();
		
		Optional<Itinerary> optionalItinerary = itineraryRepository.findById(id);
		if(!optionalItinerary.isPresent()) {
			itineraryResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return itineraryResponseDTO;
		}
		Itinerary itinerary = optionalItinerary.get();
		
		itineraryResponseDTO = toItineraryResponseDTO(itinerary);
		
		return itineraryResponseDTO;
	}
	
	public GetResourceResponseDTO findItineraryGpxById(long idItinerary) {
		GetResourceResponseDTO gpxResponseDTO = new GetResourceResponseDTO();
		
		Optional<Itinerary> optionalItinerary = itineraryRepository.findById(idItinerary);
		if(!optionalItinerary.isPresent()) {
			gpxResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return gpxResponseDTO;
		}
		Itinerary itinerary = optionalItinerary.get();
		
		if(itinerary.getGpxKey() == null) {
			gpxResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_FAILURE);
			return gpxResponseDTO;
		}
		
		gpxResponseDTO = gpxDAO.getByName(itinerary.getGpxKey());
		
/*
		FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(itinerary.getGpxURL());
		if(fileSystemResource != null) {
			gpxResponseDTO.setResource(fileSystemResource);
			gpxResponseDTO.setResultMessage(new ResultMessageDTO());
				
			return gpxResponseDTO;
		}
	*/		
			
			
		return gpxResponseDTO;
	}
	
	
	public GetListItineraryResponseDTO findItineraryByIdUser(Long idUser, int page) {
		GetListItineraryResponseDTO listItineraryResponseDTO = new GetListItineraryResponseDTO();
		
		if(idUser == null || idUser < 0) {
			listItineraryResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST);
			return listItineraryResponseDTO;
		}
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(!optionalUser.isPresent()) {
			listItineraryResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return listItineraryResponseDTO;
		}
		
		Pageable pageable = PageRequest.of(page, ITINERARY_PER_PAGE);
		List<Itinerary> itineraries = itineraryRepository.findByUser_id(idUser, pageable);
		
		listItineraryResponseDTO = toListItineraryResponseDTO(itineraries);
		return listItineraryResponseDTO;
	}
		
	public GetListItineraryResponseDTO findRandomItineraries() {
		
		Pageable pageable = PageRequest.of(0, ITINERARY_PER_PAGE);
		List<Itinerary> itineraries = itineraryRepository.findRandom(pageable);
		GetListItineraryResponseDTO listItineraryResponseDTO = toListItineraryResponseDTO(itineraries);
		
		return listItineraryResponseDTO;
	}

	
	//SEARCHs
	public GetListItineraryResponseDTO searchItineraryByName(String name, int page) {
		Pageable pageable = PageRequest.of(page, ITINERARY_PER_PAGE);
		List<Itinerary> itineraries = itineraryRepository.findByNameContaining(name,pageable);
		
		System.out.println("size: " + itineraries.size());
		
		GetListItineraryResponseDTO listItineraryResponseDTO = toListItineraryResponseDTO(itineraries);

		return listItineraryResponseDTO;
	}
		
		
	//REMOVEs
	public ResultMessageDTO removeItineraryById(long id) {
		Optional<Itinerary> optionalItinerary = itineraryRepository.findById(id);
		if(!optionalItinerary.isPresent()) {
			return ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND;
		}
		Itinerary itinerary = optionalItinerary.get();
		String gpxUrl = itinerary.getGpxKey();
		
		itineraryRepository.delete(itinerary);
		gpxDAO.delete(gpxUrl);
		
		/*
		try {
			fileSystemRepository.delete(gpxUrl);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
	
	//----------------------------------------------------------------------
	
	//MAPPER

	public Itinerary toItineraryEntity(SaveItineraryRequestDTO itineraryDTO) {
		
		Itinerary itinerary = new Itinerary();
		itinerary.setName(itineraryDTO.getName());
		itinerary.setDescription(itineraryDTO.getDescription());
		itinerary.setDifficulty(itineraryDTO.getDifficulty());
		itinerary.setDuration(itineraryDTO.getDuration());
		itinerary.setLenght(itineraryDTO.getLenght());
		
		Optional<User> user = userRepository.findById(itineraryDTO.getIdUser());
		if(!user.isPresent()) {
			return null;
		}
		itinerary.setUser(user.get());
		
		itinerary.setGpxKey("");
		
		return itinerary;
	}
	

	
	
	
	//----
	
	public GetItineraryResponseDTO toItineraryResponseDTO(Itinerary itinerary) {
		
		if(itinerary == null) return null;
		
		GetItineraryResponseDTO dto = new GetItineraryResponseDTO();
		
		dto.setId(itinerary.getId());
		dto.setName(itinerary.getName());
		dto.setDescription(itinerary.getDescription());
		dto.setDifficulty(itinerary.getDifficulty());
		dto.setDuration(itinerary.getDuration());
		dto.setLenght(itinerary.getLenght());
		
		dto.setIdUser(itinerary.getUser().getId());
		
		dto.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
		
		return dto;
	}
	
	
	public GetListItineraryResponseDTO toListItineraryResponseDTO(List<Itinerary> itineraries){
		
		GetListItineraryResponseDTO listItineraryResponseDTO = new GetListItineraryResponseDTO();
		List<GetItineraryResponseDTO> itinerariesDTO = new LinkedList<GetItineraryResponseDTO>();
		
		
		if(itineraries == null) {
			listItineraryResponseDTO.setListItinerary(null);
			listItineraryResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
			return listItineraryResponseDTO;
		}
		
		
		for(Itinerary itinerary : itineraries) {
			itinerariesDTO.add(toItineraryResponseDTO(itinerary));
		}
		
		listItineraryResponseDTO.setListItinerary(itinerariesDTO);
		listItineraryResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
		return listItineraryResponseDTO;
	}
	
	
	
	//VALIDATORs	
	public boolean isValidDTO(SaveItineraryRequestDTO itineraryDTO) {
		
		if(itineraryDTO == null) return false;
				
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
			return false;
		}
		

		if(itineraryDTO.getIdUser() < 0) return false;
			
		Optional<User> optionalUser = userRepository.findById(itineraryDTO.getIdUser());
		if(optionalUser.isEmpty()) return false;
		
		return true;
	}





	


	

	
	
	
	
	
}
