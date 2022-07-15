package com.natour.server.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.natour.server.data.entities.User;




@Repository
public interface UserRepository extends JpaRepository<User, Long>{	
	//findByUsername()
	
	//updateProfileImageURL()
	
	User findByUsername(String username);
	
	@Transactional
	@Modifying
	@Query("update User u set u.profileImageURL = :profileImageURL where u.username = :username")
	User updateProfileImageURL(@Param("username") String username,@Param("profileImageURL") String profileImageURL);
	
}
