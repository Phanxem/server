package com.natour.server.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.natour.server.data.entities.Itinerary;
import com.natour.server.data.entities.User;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long>{

	//DA TESTARE
	List<Itinerary> findByNameContaining(String name);

	//DA TESTARE
	List<Itinerary> findByUser_id(long idUser);
	
	//DA TESTARE
	List<Itinerary> findByUser(User user);

	//DA TESTARE
	List<Itinerary> findByUser_username(String usernameUser);

	//DA TESTARE
	@Transactional
	@Modifying
	@Query("update Itinerary i " + 
		   "set i.gpxURL = :gpxURL" +
		   "where i.id = :id" )
	int updateGPXFileURL(@Param("id") long id,@Param("gpxURL") String gpxURL);
	
}
