package com.natour.server.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.natour.server.data.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Long>{

	//DA TESTARE
	List<Report> findByItinerary_id(long idItinerary);

}
