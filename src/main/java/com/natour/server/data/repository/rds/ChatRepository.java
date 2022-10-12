package com.natour.server.data.repository.rds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.natour.server.data.entities.rds.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>{

}
