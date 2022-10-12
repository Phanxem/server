package com.natour.server.data.repository.dynamoDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.natour.server.data.entities.dynamoDB.ChatConnection;
import com.natour.server.data.entities.rds.Chat;

@Repository
public class ChatConnectionRepository {

	
	@Autowired
	private DynamoDBMapper dynamoDBMapper;
	
	
	public ChatConnection findById(String id) {
		ChatConnection result = dynamoDBMapper.load(ChatConnection.class,id);
		
		return result;
	}


	public ChatConnection add(String id) {
		ChatConnection chatConnection = new ChatConnection();
		chatConnection.setIdConnection(id);
		dynamoDBMapper.save(chatConnection);
		
		return chatConnection;
	}


	public ChatConnection updateWithIdUser(String id, String idUser) {
		ChatConnection chatConnection = new ChatConnection();
		chatConnection.setIdConnection(id);
		chatConnection.setIdUser(idUser);
		dynamoDBMapper.save(chatConnection);
		
		return chatConnection;
	}


	public ChatConnection findByIdUser(String idUser) {
		Map<String, AttributeValue> expressionAttributeValue = new HashMap<String, AttributeValue>();
	    expressionAttributeValue.put(":v1", new AttributeValue().withS(idUser));
	    
	    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
	            .withFilterExpression("idUser = :v1")
	            .withExpressionAttributeValues(expressionAttributeValue);
	    
	    PaginatedScanList<ChatConnection> list = dynamoDBMapper.scan(ChatConnection.class, scanExpression);
	    ChatConnection result = list.get(0);
		return result;
	}


	public void delete(String id) {
		ChatConnection result = dynamoDBMapper.load(ChatConnection.class,id);
		dynamoDBMapper.delete(result);
	}
	
	
	
	
	
	
	
	
	
	/*
	public long findIdUserByIdConnection(String idConnection) {
		// TODO Auto-generated method stub
		return 1;
	}
*/
	
	
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
