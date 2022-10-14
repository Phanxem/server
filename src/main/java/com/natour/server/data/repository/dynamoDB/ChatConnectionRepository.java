package com.natour.server.data.repository.dynamoDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApi;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.natour.server.data.entities.dynamoDB.ChatConnection;
import com.natour.server.data.entities.rds.Chat;
import com.natour.server.data.entities.rds.Message;

@Repository
public class ChatConnectionRepository {

	@Autowired
	private AmazonDynamoDB amazonDynamoDB;
	
	@Autowired
	private DynamoDBMapper dynamoDBMapper;
	
	
	private static final String TABLE_NAME = "ChatConnection";
	
	private static final String KEY_ID_USER = "idUser";
	
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
	
		//ChatConnection result = dynamoDBMapper.load(ChatConnection.class,id);
		
		Map<String, AttributeValue> expressionAttributeValue = new HashMap<String, AttributeValue>();
	    expressionAttributeValue.put(":v1", new AttributeValue().withS(idUser));
	    
	    DynamoDBQueryExpression<ChatConnection> queryExpression = new DynamoDBQueryExpression<ChatConnection>()
	    	    .withIndexName(KEY_ID_USER + "-index")
	    	    .withConsistentRead(false)
	    	    .withKeyConditionExpression("idUser = :v1")
	    	    .withExpressionAttributeValues(expressionAttributeValue);

	    List<ChatConnection> list =  dynamoDBMapper.query(ChatConnection.class, queryExpression);
	    
	    for(ChatConnection el : list) {
	    	System.out.println("IdConnection: " + el.getIdConnection() + " | " + "IdUser: " + el.getIdUser() + "\n");
	    }
	    
	    if(list == null || list.isEmpty()) return null;
	    
	    ChatConnection result = list.get(0);
		return result;
				
				
				
		/*
		HashMap<String,AttributeValue> keyToGet = new HashMap<>();
		
		AttributeValue attributeValue = new AttributeValue();
		attributeValue.setS(idUser);
		
		
		
        keyToGet.put(KEY_ID_USER, attributeValue);

        GetItemRequest getItemRequest = new GetItemRequest();
        getItemRequest.setKey(keyToGet);
        getItemRequest.setTableName(TABLE_NAME);
        		

        try {
        	GetItemResult getItemResult = amazonDynamoDB.getItem(getItemRequest);
        	Map<String,AttributeValue> returnedItem = getItemResult.getItem();
            if (returnedItem != null) {
                Set<String> keys = returnedItem.keySet();
                System.out.println("Amazon DynamoDB table attributes: \n");

                for (String key1 : keys) {
                    System.out.format("%s: %s\n", key1, returnedItem.get(key1).toString());
                }
            }
            else {
                System.out.format("No item found");
            }

        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
		*/
		
		
		
		
		/*
		Map<String, AttributeValue> expressionAttributeValue = new HashMap<String, AttributeValue>();
	    expressionAttributeValue.put(":v1", new AttributeValue().withS(idUser));
	    
	    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
	            .withFilterExpression("idUser = :v1")
	            .withExpressionAttributeValues(expressionAttributeValue);
	    
	    PaginatedScanList<ChatConnection> list = dynamoDBMapper.scan(ChatConnection.class, scanExpression);
	    
	    for(ChatConnection el : list) {
	    	System.out.println("IdConnection: " + el.getIdConnection() + " | " + "IdUser: " + el.getIdUser() + "\n");
	    }
	    
	    ChatConnection result = list.get(0);
		return result;
		*/
	    
	    
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
