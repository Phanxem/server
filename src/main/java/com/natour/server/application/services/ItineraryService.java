package com.natour.server.application.services;



import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natour.server.application.dtos.ItineraryDTO;
import com.natour.server.data.entities.Itinerary;
import com.natour.server.data.entities.User;
import com.natour.server.data.repository.ItineraryRepository;
import com.natour.server.data.repository.UserRepository;

@Service
public class ItineraryService {

	@Autowired
	private ItineraryRepository itineraryRepository;
	@Autowired
	private UserRepository userRepository;
	
	//ADDs
	public ItineraryDTO addItinerary(String usernameUser, ItineraryDTO itineraryDTO) {
		
		Long idUser = userRepository.findIdByUsername(usernameUser);
		
		itineraryDTO.setIdUser(idUser);
		Itinerary itinerary = toItineraryEntity(itineraryDTO);
		
		//TODO save GPX file nel filesystem;
		//aggiungi l'url del file GPX in itinerary
		
		Itinerary result = itineraryRepository.save(itinerary);
		
		return toItineraryDTO(result);
	}

	//UPDATEs
		
	
	//FINDs
	public ItineraryDTO findItineraryById(long id) {
		Optional<Itinerary> itinerary = itineraryRepository.findById(id);
		if(itinerary.isPresent()) return toItineraryDTO(itinerary.get());
		return null;
	}
	
	public List<ItineraryDTO> findItineraryByIdUser(long idUser) {
		List<Itinerary> itineraries = itineraryRepository.findByUser_id(idUser);
		
		List<ItineraryDTO> itinerariesDTO = toListItineraryDTO(itineraries);
		
		return itinerariesDTO;
	}
	
	public List<ItineraryDTO> findItineraryByUsernameUser(String usernameUser) {
		List<Itinerary> itineraries = itineraryRepository.findByUser_username(usernameUser);
		
		List<ItineraryDTO> itinerariesDTO = toListItineraryDTO(itineraries);
		
		return itinerariesDTO;
	}

		
	
	//SEARCHs
	public List<ItineraryDTO> searchItineraryByName(String name) {
		List<Itinerary> itineraries = itineraryRepository.findByNameContaining(name);
		
		List<ItineraryDTO> itinerariesDTO = toListItineraryDTO(itineraries);

		return itinerariesDTO;
	}
		
		
	//REMOVEs
	
	
	//MAPPER
	
	public ItineraryDTO toItineraryDTO(Itinerary itinerary) {
		
		if(itinerary == null) return null;
		
		ItineraryDTO itineraryDTO = new ItineraryDTO();
		itineraryDTO.setId(itinerary.getId());
		itineraryDTO.setName(itinerary.getName());
		itineraryDTO.setDescription(itinerary.getDescription());
		itineraryDTO.setDifficulty(itinerary.getDifficulty());
		itineraryDTO.setLenght(itinerary.getLenght());
		
		itineraryDTO.setIdUser(itinerary.getUser().getId());
		
		//TODOs
		itineraryDTO.setDuration(null);
		
		itineraryDTO.setWayPoints(null);
		
		return itineraryDTO;
	}


	public List<ItineraryDTO> toListItineraryDTO(List<Itinerary> itineraries){
		if(itineraries == null) return null;
		
		List<ItineraryDTO> itinerariesDTO = new LinkedList<ItineraryDTO>();
		for(Itinerary itinerary : itineraries) {
			itinerariesDTO.add(toItineraryDTO(itinerary));
		}
		
		return itinerariesDTO;
	}


	
	private Itinerary toItineraryEntity(ItineraryDTO itineraryDTO) {
		if(itineraryDTO == null) return null;
		
		Itinerary itinerary = new Itinerary();
		itinerary.setName(itineraryDTO.getName());
		itinerary.setDescription(itineraryDTO.getDescription());
		itinerary.setDifficulty(itineraryDTO.getDifficulty());
		itinerary.setLenght(itineraryDTO.getLenght());
		
		User user = userRepository.getReferenceById(itineraryDTO.getIdUser());
		itinerary.setUser(user);
		
		//TODOs
		itinerary.setDuration(null);
		
		itinerary.setWayPointsURL(null);
		
		return itinerary;
	}
	

	
}
