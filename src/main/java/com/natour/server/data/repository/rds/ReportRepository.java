package com.natour.server.data.repository.rds;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.natour.server.data.entities.rds.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>{

	//DA TESTARE
	List<Report> findByItinerary_id(long idItinerary, Pageable pageable);
	
	//List<Report> findByItinerary(Itinerary itinerary);

}
