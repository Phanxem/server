package com.natour.server.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import com.natour.server.data.entities.User;




@Repository
public interface UserRepository extends JpaRepository<User, Long>{	
	//findByUsername()
	
	//updateProfileImageURL()
	
	User findByUsername(String username);
	
	@Query("select u.id " +
		   "from User u " +
		   "where u.username = :username")
	Long findIdByUsername(@Param("username") String username);
	
	@Query("select u.username " + 
		   "from User u " +
		   "where u.id = :id")
	String findUsernameById(@Param("id") long id);
	
	
	//DA TESTARE
	List<User> findByUsernameContaining(String username);
	
	
	
	
/*	
	@Transactional
	@Modifying
	@Query("update User u " + 
		   "set u.profileImageURL = :profileImageURL " +
		   "where u.username = :username " )
	int updateProfileImageURL(@Param("username") String username,@Param("profileImageURL") String profileImageURL);
	
	//DA TESTARE
	@Transactional
	@Modifying
	@Query("update User u " +
		   "set u.placeOfResidence = :placeOfResidence, " +
			   "u.dateOfBirth = :dateOfBirth, " +
		       "u.gender = :gender " +
		   "where u.username = :username " )
	int updateOptionalInfo(@Param("username") String username,
							@Param("placeOfResidence") String placeOfResidence,
							@Param("dateOfBirth") Timestamp dateOfBirth,
							@Param("gender") String gender);

	*/
}
