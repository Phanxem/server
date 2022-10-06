package com.natour.server.application.services;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.natour.server.DateUtils;
import com.natour.server.application.dtos.request.ChatRequestDTO;
import com.natour.server.application.dtos.response.ChatResponseDTO;
import com.natour.server.application.dtos.response.ListMessageResponseDTO;
import com.natour.server.application.dtos.response.ListUserResponseDTO;
import com.natour.server.application.dtos.response.MessageResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.UserResponseDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.UserNotFoundException;
import com.natour.server.data.entities.Chat;
import com.natour.server.data.entities.ChatConnection;
import com.natour.server.data.entities.Message;
import com.natour.server.data.entities.User;
import com.natour.server.data.repository.ChatConnectionRepository;
import com.natour.server.data.repository.ChatRepository;
import com.natour.server.data.repository.MessageRepository;
import com.natour.server.data.repository.UserRepository;
import com.natour.server.presentation.restController.ChatRestController;

@Service
public class ChatService {

	private static final int MESSAGE_PER_PAGE = 50;
	
	private static final String KEY_ACTION = "action";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_MESSAGE = "message";
	
	private static final String VALUE_ACTION_INIT_CONNECTION = "initConnection";
	private static final String VALUE_ACTION_SEND_MESSAGE = "sendMessage";
	private static final String VALUE_ACTION_DISCONNECT = "disconnect";
	
	@Autowired
	private ChatConnectionRepository chatConnectionRepository;
	
	@Autowired
	private ChatRepository chatRepository;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private UserRepository userRepository;
	

	public ListMessageResponseDTO findMessagesByIdChat(long idChat, int page) {
		Optional<Chat> optionalChat = chatRepository.findById(idChat);
		if(optionalChat.isEmpty()) {
			//TODO
			return null;
		}
		//Chat chat = optionalChat.get();
		
		Pageable pageable = PageRequest.of(page, MESSAGE_PER_PAGE);
		List<Message> messages = messageRepository.findByChat_idOrderByDateOfInputDesc(idChat, pageable);
		
		return toListMessageResponseDTO(messages);
	}
	
	

	public ResultMessageDTO sendMessage(ChatRequestDTO chatRequestDTO) {

		Map<String,String> payload = chatRequestDTO.getPayload();
		
		String idConnection = chatRequestDTO.getConnectionId();
		
		String stringIdUser = payload.get("idUser");
		long idUserDestination = Long.valueOf(stringIdUser);
		String bodyMessage = payload.get("message");
		String stringInputTime = payload.get("inputTime");
		Timestamp inputTime;
		try {
			inputTime = DateUtils.toTimestamp(stringInputTime);
		} catch (ParseException e) {
			// TODO
			e.printStackTrace();
			return null;
		}
		
		long idUserSource = chatConnectionRepository.findIdUserByIdConnection(idConnection);
		
		
		
		
		Optional<User> optionalUserSource = userRepository.findById(idUserSource);
		Optional<User> optionalUserDestination = userRepository.findById(idUserDestination);
		if(optionalUserSource.isEmpty() || optionalUserDestination.isEmpty()) {
			//TODO
			return null;
		}
		User userSource = optionalUserSource.get();
		User userDestination = optionalUserDestination.get();
		
		
		List<Chat> chats1 = userSource.getChats();
		List<Chat> chats2 = userDestination.getChats();
		
		List<Chat> intersection = new LinkedList<Chat>();
		intersection.addAll(chats1);
		intersection.retainAll(chats2);
		
		
		Chat chat = null;
		if(intersection.isEmpty()) {
			Chat tempChat = new Chat();
			tempChat.setMessages(new ArrayList<Message>());
			List<User> users = new ArrayList<User>();
			users.add(userSource);
			users.add(userDestination);
			
			tempChat.setUsers(users);
			
			chat = chatRepository.save(tempChat);
		}
		else chat = intersection.get(0);
		
		
		Message message = new Message();
		
		message.setUser(userSource);
		message.setChat(chat);
		message.setBody(bodyMessage);
		message.setDateOfInput(inputTime);
		
		messageRepository.save(message);
		
		
		
		/*
		TODO
		inviare il messaggio via socket
		 */
		
		return new ResultMessageDTO();
	}
	
	
	public ChatResponseDTO findChatByIdsUser(long idUser1, long idUser2) {
		
		if(idUser1 < 0 || idUser2 < 0) {
			//TODO 
			return null;
		}
		
		Optional<User> optionalUser1 = userRepository.findById(idUser1);
		Optional<User> optionalUser2 = userRepository.findById(idUser2);
		if(optionalUser1.isEmpty() || optionalUser2.isEmpty()) {
			//TODO
			return null;
		}
		User user1 = optionalUser1.get();
		User user2 = optionalUser2.get();
		
		List<Chat> chats1 = user1.getChats();
		List<Chat> chats2 = user2.getChats();
		
		List<Chat> intersection = new LinkedList<Chat>();
		intersection.addAll(chats1);
		intersection.retainAll(chats2);
		
		if(intersection.isEmpty()) {
			//TODO
			return null;
		}
		
		Chat chat = intersection.get(0);
		
		return toChatResponseDTO(chat);
	}

	
	//search indicando gli id di uno o più utenti si recuperano le relative chat
	
	
	
	
	
	
	
	
	
	
	
	
		
	public MessageResponseDTO toMessageResponseDTO(Message message){
		if(message == null) return null;
			
		MessageResponseDTO dto = new MessageResponseDTO();
		dto.setId(message.getId());
			
		Date dateOfInput = new Date(message.getDateOfInput().getTime());
		DateFormat dateFormat = new SimpleDateFormat();
		String stringDate = dateFormat.format(dateOfInput);
		dto.setDateOfInput(stringDate);
			
		dto.setBody(message.getBody());
			
		dto.setIdUser(message.getUser().getId());
		dto.setIdChat(message.getChat().getId());
		
		return dto;
	}
	
	public ListMessageResponseDTO toListMessageResponseDTO(List<Message> messages) {
		if(messages == null) return null;
		
		List<MessageResponseDTO> dto = new LinkedList<MessageResponseDTO>();
		for(Message message : messages) {
			dto.add(toMessageResponseDTO(message));
		}
		
		ListMessageResponseDTO listMessageResponseDTO = new ListMessageResponseDTO();
		listMessageResponseDTO.setListMessage(dto);
		listMessageResponseDTO.setResultMessage(new ResultMessageDTO());
		
		return listMessageResponseDTO;
	}

	public ChatResponseDTO toChatResponseDTO(Chat chat) {
		if(chat == null) return null;
		
		ChatResponseDTO dto = new ChatResponseDTO();
		dto.setId(chat.getId());
		dto.setResultMessage(new ResultMessageDTO());	
		
		return dto;
		
	}



	

	
	
	
	
	
	
	
	
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
		
		//---
		Map<String, String> massimo = new HashMap<String, String>();
		
		massimo.put("action", "initConnection");
		massimo.put("username", "francescoPotentissimo");

		chatRequestDTO.setConnectionId("sedano argdente");
		chatRequestDTO.setPayload(massimo);
		//---
		
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
