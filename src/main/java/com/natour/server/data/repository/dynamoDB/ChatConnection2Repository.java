package com.natour.server.data.repository.dynamoDB;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.natour.server.data.entities.dynamoDB.ChatConnection;

@EnableScan
public interface ChatConnection2Repository extends CrudRepository<ChatConnection, String> {

}
