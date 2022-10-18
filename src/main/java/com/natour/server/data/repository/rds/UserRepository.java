package com.natour.server.data.repository.rds;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.natour.server.data.entities.rds.User;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{	
	
	
	User findByIdentityProviderAndIdIdentityProvided(String identityProvider, String idIdentityProvided);
	
	//DA TESTARE
	//ricerca utente
	List<User> findByUsernameContaining(String username, Pageable pageble);

	
	
}
