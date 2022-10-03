package com.natour.server.data.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.natour.server.data.entities.ChatConnection;

@Repository
public class ChatConnectionRepository {

	/*
	
	@Autowired
	private DynamoDBMapper dynamoDBMapper;
	
	
	
	public ChatConnection save(ChatConnection chatConnection) {
		dynamoDBMapper.save(chatConnection);
		return chatConnection;
	}
	
	
	public ChatConnection getChatConnectionByConnectionId(String connectionId) {
		ChatConnection result = dynamoDBMapper.load(ChatConnection.class, connectionId);
		return result;
	}
	
	
	public List<ChatConnection> getChatConnectionByUsername(String username){
		DynamoDBQueryExpression<ChatConnection> queryExpression = new DynamoDBQueryExpression<ChatConnection>()
		          .withKeyConditionExpression("username = " + username);

		
		List<ChatConnection> result = dynamoDBMapper.query(ChatConnection.class, queryExpression);
		
		return result;
	}
	
	
	public void deleteByConnectionId(String connectionId) {
		ChatConnection chatConnection = getChatConnectionByConnectionId(connectionId);
		dynamoDBMapper.delete(chatConnection);
	}
	
	
	public void deleteByUsername(String username) {
		List<ChatConnection> chatConnections = getChatConnectionByUsername(username);
		for(ChatConnection chatConnection : chatConnections) {
			dynamoDBMapper.delete(chatConnection);
		}
	}
	
	*/
	
	
	
	

	
	
}
