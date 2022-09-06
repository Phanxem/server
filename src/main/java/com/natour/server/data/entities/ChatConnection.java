package com.natour.server.data.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ChatConnection")
public class ChatConnection {
	
	@DynamoDBHashKey
	private String connectionId;
	@DynamoDBAttribute
	private String username;
	
	public ChatConnection() {}
	
	public ChatConnection(String connectionId, String username) {
		this.connectionId = connectionId;
		this.username = username;
	}
	
	
	public String getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
