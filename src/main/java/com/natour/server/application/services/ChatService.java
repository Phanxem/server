package com.natour.server.application.services;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.AwsSyncClientParams;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApi;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiAsync;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiAsyncClientBuilder;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiClient;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiClientBuilder;
import com.amazonaws.services.apigatewaymanagementapi.model.PostToConnectionRequest;
import com.google.gson.JsonObject;
import com.natour.server.application.dtos.MessageDTO;
import com.natour.server.application.dtos.SuccessMessageDTO;
import com.natour.server.application.dtos.request.ChatRequestDTO;
import com.natour.server.data.entities.ChatConnection;
import com.natour.server.data.repository.ChatConnectionRepository;
import com.natour.server.presentation.restController.ChatRestController;

@Service
public class ChatService {

	private static final String KEY_ACTION = "action";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_MESSAGE = "message";
	
	private static final String VALUE_ACTION_INIT_CONNECTION = "initConnection";
	private static final String VALUE_ACTION_SEND_MESSAGE = "sendMessage";
	private static final String VALUE_ACTION_DISCONNECT = "disconnect";
	
	@Autowired
	private ChatConnectionRepository chatConnectionRepository;
	
	/*
	public static Map<String,String> connectionId_username = new HashMap<String,String>();
	
	
	String url = "https://hren0i7ir6.execute-api.eu-west-1.amazonaws.com/production";
	String region = "eu-west-1";
	
	EndpointConfiguration endpointConfiguration = new EndpointConfiguration(url, region);
    AmazonApiGatewayManagementApi api =
        AmazonApiGatewayManagementApiClientBuilder
            .standard()
            .withEndpointConfiguration(endpointConfiguration)
            .build();
*/
    
    /*
    PostToConnectionRequest request =
        new PostToConnectionRequest()
            .withConnectionId(terminal.getConnectionId())
            .withData(ByteBuffer.wrap(token.getBytes()));

    api.postToConnectionAsync(request).get();
*/	
	
	/*
	@Autowired
	AmazonApiGatewayManagementApi apiGatewayManagement;
*/
	
	
	 /*
	 AmazonApiGatewayManagementApiClientBuilder builder = AmazonApiGatewayManagementApiClientBuilder.standard();
     String endpoint = "https://hren0i7ir6.execute-api.eu-west-1.amazonaws.com/production";

     AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
             endpoint, "eu-west-1"
     );

     AmazonApiGatewayManagementApi apiGatewayManagement = builder
             .withEndpointConfiguration(endpointConfiguration)
             .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
             .build();
*/
     
	
	
	//---------
	
	/*
	
	public MessageDTO addConnection() {		
		return new SuccessMessageDTO();
	}

	
	public MessageDTO initConnection(ChatRequestDTO chatRequest2DTO) {
		
		ChatRequestDTO chatRequestDTO = new ChatRequestDTO();
		
		Map<String, String> massimo = new HashMap<String, String>();
		
		massimo.put("action", "initConnection");
		massimo.put("username", "francescoPotentissimo");

		chatRequestDTO.setConnectionId("sedano argdente");
		chatRequestDTO.setPayload(massimo);
		
		
		if(!isValid(chatRequestDTO)) {
			//TODO throw exception
			return null; //test
		}
		

		String connectionId = chatRequestDTO.getConnectionId();
		String username = chatRequestDTO.getPayload().get(KEY_USERNAME);
		
		ChatConnection chatConnection = new ChatConnection(connectionId,username);
		System.out.println("16");
		ChatConnection result = chatConnectionRepository.save(chatConnection);
		System.out.println("17");
		if(result == null) return null;
		
		return new SuccessMessageDTO();
	}
*/
	/*
	//TODO da completare con ApiGatewayManagement
	public MessageDTO sendMessage(ChatRequestDTO chatRequestDTO) {
		if(!isValid(chatRequestDTO)) {
			
		}
		*/
		/*
		if(connectionId_username == null || connectionId_username.isEmpty()) System.out.println("map empty");
		
		
		for (Map.Entry<String, String> entry : connectionId_username.entrySet()) {
		    System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		
		String connectionId = chatRequestDTO.getConnectionId();
		String message = chatRequestDTO.getPayload().get("message");
		String username = chatRequestDTO.getPayload().get("username");
		
		//String connectionId = 
		*/
		/*
		PostToConnectionRequest request =
		        new PostToConnectionRequest()
		            .withConnectionId(connectionId)
		            .withData(ByteBuffer.wrap(message.getBytes()));

		    api.postToConnection(request);
		
*/		
		    /*
		PostToConnectionRequest request = new PostToConnectionRequest();
        request.withConnectionId(connectionId);
        request.withData(ByteBuffer.wrap(message.getBytes()));
        //request.withData(ByteBuffer.wrap("{\"message\":\"Receive Booking Request\", \"status\":\"finished\"}".getBytes()));
        apiGatewayManagement.postToConnection(request);
*/
		
		
		/*
		
		return new SuccessMessageDTO();
	}
*/
		
	/*
	public MessageDTO removeConnection(ChatRequestDTO chatRequestDTO) {
		
		String connectionId = chatRequestDTO.getConnectionId();
		
		chatConnectionRepository.deleteByConnectionId(connectionId);
		
		return new SuccessMessageDTO();
	}
*/
	
	
	
	
	
	/*
	public boolean isValid(ChatRequestDTO chatRequestDTO) {

		if(chatRequestDTO == null) return false;

		String connectionId = chatRequestDTO.getConnectionId();
		if(connectionId == null || connectionId.isEmpty()) return false;

		Map<String, String> payload = chatRequestDTO.getPayload();
		if(payload == null || payload.isEmpty()) return false;
		
		if(!payload.containsKey(KEY_ACTION)) return false;

		String action = payload.get(KEY_ACTION);
		if(action.equals(VALUE_ACTION_INIT_CONNECTION)) {
			if(payload.size() != 2) return false;
			if(!payload.containsKey(KEY_USERNAME)) return false;
			String username = payload.get(KEY_USERNAME);
			if(username == null || username.isEmpty()) return false;

		}
		else if(action.equals(VALUE_ACTION_SEND_MESSAGE)) {
			if(payload.size() != 3) return false;
			if(!payload.containsKey(KEY_USERNAME)) return false;
			String username = payload.get(KEY_USERNAME);
			if(username == null || username.isEmpty()) return false;

			if(!payload.containsKey(KEY_MESSAGE)) return false;
			String message = payload.get(KEY_MESSAGE);
			if(username == null || username.isEmpty()) return false;

		}
		else if(action.equals(VALUE_ACTION_DISCONNECT)) {
			if(payload.size() != 1) return false;

		}
		else return false;
		
		return true;
	}
	
	*/
	
}
