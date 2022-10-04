package com.natour.server.data.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.natour.server.data.entities.Itinerary;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long>{

	final static int ELEMENT_PER_PAGE = 10;
	
	//DA TESTARE
	//ricerca itinerario
	List<Itinerary> findByNameContaining(String name, Pageable pageable);

	//DA TESTARE
	List<Itinerary> findByUser_id(long idUser, Pageable pageable);
	
	@Query("select * " +
		   "from Itinerary i " +
		   "order by RAND() " +
		   "limit " + ELEMENT_PER_PAGE)
	List<Itinerary> findRandom();
	
	
	
	
	
	//DA TESTARE
	//List<Itinerary> findByUser(User user);

	//DA TESTARE
	//List<Itinerary> findByUser_username(String usernameUser);

	/*TODO usare save invece di questa funzione
	//DA TESTARE
	@Transactional
	@Modifying
	@Query("update Itinerary i " + 
		   "set i.gpxURL = :gpxURL " +
		   "where i.id = :id " )
	int updateGPXFileURL(@Param("id") long id,@Param("gpxURL") String gpxURL);
	*/
}
