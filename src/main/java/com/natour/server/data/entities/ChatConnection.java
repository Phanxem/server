package com.natour.server.data.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "ChatConnection")
public class ChatConnection {
	
	@DynamoDBHashKey
	private String idConnection;
	@DynamoDBAttribute
	private String idUser;
	
	public ChatConnection() {}
	
	public ChatConnection(String idConnection, String idUser) {
		this.idConnection = idConnection;
		this.idUser = idUser;
	}

	public String getIdConnection() {
		return idConnection;
	}

	public void setIdConnection(String idConnection) {
		this.idConnection = idConnection;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	
	
}
