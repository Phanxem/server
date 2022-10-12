package com.natour.server.data.repository.rds;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.natour.server.data.entities.rds.Message;
import com.natour.server.data.entities.rds.Report;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

	List<Message> findByChat_idOrderByDateOfInputDesc(long idChat, Pageable pageable);
}
