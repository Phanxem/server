package com.natour.server.data.repository.rds;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.natour.server.data.entities.rds.Itinerary;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long>{
	
	List<Itinerary> findByNameContaining(String name, Pageable pageable);

	List<Itinerary> findByUser_id(long idUser, Pageable pageable);
	
	@Query("select i " +
		   "from Itinerary i " +
		   "order by rand() " )
	List<Itinerary> findRandom(Pageable pageable);
	
	
	
	
	

}
