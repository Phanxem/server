package com.natour.server.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.natour.server.data.entities.Itinerary;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long>{

	//DA TESTARE
	List<Itinerary> findByNameContaining(String name);

	//DA TESTARE
	List<Itinerary> findByUser_id(long idUser);

	//DA TESTARE
	List<Itinerary> findByUser_username(String usernameUser);

	
	
}
