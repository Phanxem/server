package com.natour.server.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.natour.server.data.entities.Chat;
import com.natour.server.data.entities.Itinerary;
import com.natour.server.data.entities.Message;


@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>{

	@Query("select * " +
		   "from Chat c " +
		   "inner join ( " +
		   	"select uc.idChat " +
		   	"from User_chat uc " +
		   	"where uc.idUser = :userId " +
		   ") as sc " +
		   "on c.id = sc.idChat"
	)
	List<Chat> findByUserId(@Param("userId") long userId);

	
}
