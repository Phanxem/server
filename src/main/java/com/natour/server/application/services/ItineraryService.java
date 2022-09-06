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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.natour.server.application.dtos.MessageDTO;
import com.natour.server.application.dtos.SuccessMessageDTO;
import com.natour.server.application.dtos.request.ItineraryRequestDTO;
import com.natour.server.application.dtos.response.ItineraryResponseDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.UserIdNullException;
import com.natour.server.application.exceptionHandler.serverExceptions.FileConvertionFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.ItineraryDTOInvalidException;
import com.natour.server.application.exceptionHandler.serverExceptions.ItineraryGPXFileSaveFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.ItineraryGPXFileUpdateFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.ItineraryNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserUsernameNullException;
import com.natour.server.data.entities.Itinerary;
import com.natour.server.data.entities.User;
import com.natour.server.data.repository.FileSystemRepository;
import com.natour.server.data.repository.ItineraryRepository;
import com.natour.server.data.repository.UserRepository;

import io.jenetics.jpx.GPX;

@Service
public class ItineraryService {

	private final static long SUCCESSFUL_REMOVAL_CODE = 100;
	private final static String SUCCESSFUL_REMOVAL_MESSAGE = "Itinerario rimosso con successo.";
	
	@Autowired
	private ItineraryRepository itineraryRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FileSystemRepository fileSystemRepository;
	
	//ADDs
	public MessageDTO addItinerary(String usernameUser, ItineraryRequestDTO itineraryRequestDTO) {
		
		if(usernameUser == null) throw new UserUsernameNullException();
		
		Long idUser = userRepository.findIdByUsername(usernameUser);
		if(idUser == null) throw new UserNotFoundException();
		itineraryRequestDTO.setIdUser(idUser);
		
		
		if(!isValidDTO(itineraryRequestDTO)) throw new ItineraryDTOInvalidException();
		
		Itinerary itinerary = toItineraryEntityWithoutGPXUrl(itineraryRequestDTO);
		itinerary = itineraryRepository.save(itinerary);

		
		String gpxName = "gpx-" + itinerary.getId();
		MultipartFile gpx = itineraryRequestDTO.getGpx();
		
		
		String gpxUrl;	
		try {
			gpxUrl = fileSystemRepository.save(gpxName, gpx.getBytes());
		}
		catch (IOException e) {
			throw new ItineraryGPXFileSaveFailureException(e);
		}
		itinerary.setGpxURL(gpxUrl);
		
		
		Itinerary result = itineraryRepository.save(itinerary);
		
		return new SuccessMessageDTO();
	}

	
	//UPDATEs
	public MessageDTO updateItineraray(long id, ItineraryRequestDTO itineraryRequestDTO) {
		
		Optional<Itinerary> itinerary = itineraryRepository.findById(id);
		if(!itinerary.isPresent()) throw new ItineraryNotFoundException();
				
		if(!isValidDTO(itineraryRequestDTO)) throw new ItineraryDTOInvalidException();

		/*
		GPX gpx = itineraryDTO.getGpx();
		byte[] gpxByte = toArrayByte(gpx, "gpx-" + id);
		*/
		
		MultipartFile gpx = itineraryRequestDTO.getGpx();
		
		//non necessario
		//TODO rimuovi il file dal fileSystem;
		
		
		String gpxUrl;	
		try {
			gpxUrl = fileSystemRepository.save("gpx-" + id, gpx.getBytes());
		}
		catch (IOException e) {
			throw new ItineraryGPXFileSaveFailureException(e);
		}
		
		//non necessario?
		int updatedElement = itineraryRepository.updateGPXFileURL(id,gpxUrl);
		if(updatedElement == 0) throw new ItineraryGPXFileUpdateFailureException();
		
	
		Optional<Itinerary> result = itineraryRepository.findById(id);
		return new SuccessMessageDTO();
	}
	
	
	
	
	//FINDs
	public ItineraryResponseDTO findItineraryById(long id) {
		Optional<Itinerary> itinerary = itineraryRepository.findById(id);
		//TODO DA TESTARE
		if(!itinerary.isPresent()) throw new ItineraryNotFoundException(); 
		
		return toItineraryResponseDTO(itinerary.get());
	}
	
	public List<ItineraryResponseDTO> findItineraryByIdUser(Long idUser) {
		if(idUser == null) throw new UserIdNullException();
		
		Optional<User> user = userRepository.findById(idUser);
		if(!user.isPresent()) throw new UserNotFoundException();
		
		//List<Itinerary> itineraries = itineraryRepository.findByUser_id(user.get().getId());
		List<Itinerary> itineraries = itineraryRepository.findByUser(user.get());
		List<ItineraryResponseDTO> itinerariesDTO = toListItineraryResponseDTO(itineraries);
		
		return itinerariesDTO;
	}
	
	public List<ItineraryResponseDTO> findItineraryByUsernameUser(String usernameUser) {
		if(usernameUser == null) throw new UserUsernameNullException();
		
		User user = userRepository.findByUsername(usernameUser);
		if(user == null) throw new UserNotFoundException();
		
		//List<Itinerary> itineraries = itineraryRepository.findByUser_username(usernameUser);
		List<Itinerary> itineraries = itineraryRepository.findByUser(user);
		List<ItineraryResponseDTO> itinerariesDTO = toListItineraryResponseDTO(itineraries);
		
		return itinerariesDTO;
	}

		
	
	//SEARCHs
	public List<ItineraryResponseDTO> searchItineraryByName(String name) {
		List<Itinerary> itineraries = itineraryRepository.findByNameContaining(name);
		
		List<ItineraryResponseDTO> itinerariesDTO = toListItineraryResponseDTO(itineraries);

		return itinerariesDTO;
	}
		
		
	//REMOVEs
	public MessageDTO removeItineraryById(long id) {
		
		Optional<Itinerary> itinerary = itineraryRepository.findById(id);
		if(!itinerary.isPresent()) throw new ItineraryNotFoundException();
		
		itineraryRepository.delete(itinerary.get());
		
		MessageDTO message = new MessageDTO(SUCCESSFUL_REMOVAL_CODE, SUCCESSFUL_REMOVAL_MESSAGE);
		return message;
	}
	
	//----------------------------------------------------------------------
	
	//MAPPER
	/*
	public ItineraryDTO toItineraryDTO(Itinerary itinerary) {
		
		if(itinerary == null) return null;
		
		ItineraryDTO itineraryDTO = new ItineraryDTO();
		itineraryDTO.setId(itinerary.getId());
		itineraryDTO.setName(itinerary.getName());
		itineraryDTO.setDescription(itinerary.getDescription());
		itineraryDTO.setDifficulty(itinerary.getDifficulty());
		itineraryDTO.setDuration(itinerary.getDuration());
		itineraryDTO.setLenght(itinerary.getLenght());
		
		itineraryDTO.setIdUser(itinerary.getUser().getId());
		
		FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(itinerary.getGpxURL());
		byte[] gpx = FileSystemUtils.toArrayByte(fileSystemResource);
		itineraryDTO.setGpx(gpx);
		
		return itineraryDTO;
	}


	public List<ItineraryDTO> toListItineraryDTO(List<Itinerary> itineraries){
		if(itineraries == null) return null;
		
		List<ItineraryDTO> itinerariesDTO = new LinkedList<ItineraryDTO>();
		
		if(itineraries.isEmpty()) return itinerariesDTO;
		
		for(Itinerary itinerary : itineraries) {
			itinerariesDTO.add(toItineraryDTO(itinerary));
		}
		
		return itinerariesDTO;
	}

*/
	/*
	public Itinerary toItineraryEntity(ItineraryDTO itineraryDTO) {
		
		Itinerary itinerary = new Itinerary();
		itinerary.setName(itineraryDTO.getName());
		itinerary.setDescription(itineraryDTO.getDescription());
		itinerary.setDifficulty(itineraryDTO.getDifficulty());
		itinerary.setLenght(itineraryDTO.getLenght());
		
		Optional<User> user = userRepository.findById(itineraryDTO.getIdUser());
		if(!user.isPresent()) throw new UserNotFoundException();
		itinerary.setUser(user.get());
		
		//TODOs
		itinerary.setDuration(null);
		
		itinerary.setGpxURL("");
		
		return itinerary;
	}
	*/
	
	public Itinerary toItineraryEntityWithoutGPXUrl(ItineraryRequestDTO itineraryDTO) {
		
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
		
		FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(itinerary.getGpxURL());
		dto.setGpx(fileSystemResource);
		
		return dto;
	}
	
	
	public List<ItineraryResponseDTO> toListItineraryResponseDTO(List<Itinerary> itineraries){
		if(itineraries == null) return null;
		
		List<ItineraryResponseDTO> dto = new LinkedList<ItineraryResponseDTO>();
		
		if(itineraries.isEmpty()) return dto;
		
		for(Itinerary itinerary : itineraries) {
			dto.add(toItineraryResponseDTO(itinerary));
		}
		
		return dto;
	}
	
	
	
	//VALIDATORs
	
	public boolean isValidDTO(ItineraryRequestDTO itineraryDTO) {
		
		if(itineraryDTO == null) return false;
				
		if(itineraryDTO.getName() == null) System.out.println("errore nome");
		

		try {
			if(itineraryDTO.getName() == null ||
			   itineraryDTO.getDifficulty() == null ||
			   itineraryDTO.getDifficulty() < 0 ||
			   itineraryDTO.getDifficulty() > 2 ||
			   itineraryDTO.getLenght() == null ||
			   itineraryDTO.getDuration() == null ||
			   itineraryDTO.getGpx() == null ||
			   itineraryDTO.getGpx().getBytes().length == 0 ||
			   itineraryDTO.getIdUser() == null )
			{
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
