package com.natour.server.data.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.natour.server.data.entities.Message;
import com.natour.server.data.entities.Report;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

	List<Message> findByChat_id(long idChat, Pageable pageable);
}
