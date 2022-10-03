package com.natour.server.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.natour.server.data.entities.Chat;


@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>{


}
