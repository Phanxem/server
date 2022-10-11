package com.natour.server.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.natour.server.data.entities.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long>{

}
