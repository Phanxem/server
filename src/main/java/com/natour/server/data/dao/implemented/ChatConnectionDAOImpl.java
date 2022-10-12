package com.natour.server.data.dao.implemented;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.natour.server.data.dao.interfaces.ChatConnectionDAO;
import com.natour.server.data.entities.dynamoDB.ChatConnection;

@Repository
public class ChatConnectionDAOImpl implements ChatConnectionDAO{
	
	@Autowired
	private DynamoDBMapper dynamoDBMapper;
	
	
	@Override
	public ChatConnection findById(String id) {
		ChatConnection result = dynamoDBMapper.load(ChatConnection.class,id);
		
		return result;
	}


	@Override
	public ChatConnection add(String id) {
		ChatConnection chatConnection = new ChatConnection();
		chatConnection.setIdConnection(id);
		dynamoDBMapper.save(chatConnection);
		
		return chatConnection;
	}


	@Override
	public ChatConnection updateWithIdUser(String id, String idUser) {
		ChatConnection chatConnection = new ChatConnection();
		chatConnection.setIdConnection(id);
		chatConnection.setIdUser(idUser);
		dynamoDBMapper.save(chatConnection);
		
		return chatConnection;
	}


	@Override
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


	@Override
	public void delete(String id) {
		ChatConnection result = dynamoDBMapper.load(ChatConnection.class,id);
		dynamoDBMapper.delete(result);
	}
	
}
