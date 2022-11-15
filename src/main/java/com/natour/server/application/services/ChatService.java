package com.natour.server.application.services;


import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.amazonaws.services.apigatewaymanagementapi.model.PostToConnectionResult;
import com.google.gson.JsonObject;
import com.natour.server.application.dtos.request.ChatRequestDTO;
import com.natour.server.application.dtos.request.SendMessageRequestDTO;
import com.natour.server.application.dtos.response.GetChatResponseDTO;
import com.natour.server.application.dtos.response.GetIdChatResponseDTO;
import com.natour.server.application.dtos.response.GetListChatResponseDTO;
import com.natour.server.application.dtos.response.GetListChatMessageResponseDTO;
import com.natour.server.application.dtos.response.GetListUserResponseDTO;
import com.natour.server.application.dtos.response.GetChatMessageResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.GetUserResponseDTO;
import com.natour.server.application.dtos.response.HasMessageToReadResponseDTO;
import com.natour.server.application.services.utils.DateUtils;
import com.natour.server.application.services.utils.ResultMessageUtils;
import com.natour.server.data.dao.implemented.MessageDAOImpl;
import com.natour.server.data.dao.interfaces.MessageDAO;
import com.natour.server.data.entities.dynamoDB.ChatConnection;
import com.natour.server.data.entities.rds.Chat;
import com.natour.server.data.entities.rds.Message;
import com.natour.server.data.entities.rds.User;
import com.natour.server.data.repository.dynamoDB.ChatConnectionRepository;
import com.natour.server.data.repository.rds.ChatRepository;
import com.natour.server.data.repository.rds.MessageRepository;
import com.natour.server.data.repository.rds.UserRepository;
import com.natour.server.presentation.restController.ChatRestController;

@Service
public class ChatService {

	private static final int MESSAGE_PER_PAGE = 50;
	private static final int CHAT_PER_PAGE = 20;
	
	private static final String KEY_ACTION = "action";
	private static final String KEY_ID_USER = "idUser";
	private static final String KEY_MESSAGE = "message";
	private static final String KEY_INPUT_TIME = "inputTime";
	
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
	
	@Autowired
	private MessageDAO messageDAO;

	
	
	
	
	
	
	//--------------
	public ResultMessageDTO test(String idUser) {		
		ChatConnection chatConnection = chatConnectionRepository.findByIdUser(idUser);
		
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
	//--------------------
	
	
	

	public GetListChatMessageResponseDTO findMessagesByIdChat(long idChat, int page) {
		GetListChatMessageResponseDTO listMessageResponseDTO = new GetListChatMessageResponseDTO();
		
		Optional<Chat> optionalChat = chatRepository.findById(idChat);
		if(optionalChat.isEmpty()) {
			listMessageResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return listMessageResponseDTO;
		}
		
		Pageable pageable = PageRequest.of(page, MESSAGE_PER_PAGE);
		List<Message> messages = messageRepository.findByChat_idOrderByDateOfInputDesc(idChat, pageable);
		
		listMessageResponseDTO = toListMessageResponseDTO(idChat, messages);
		
		return listMessageResponseDTO;
	}

	public GetListChatResponseDTO searchByIdUser(long idUser, int page) {
		GetListChatResponseDTO listChatResponseDTO = new GetListChatResponseDTO();
		
		if(idUser < 0) {
			listChatResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST);
			return listChatResponseDTO;
		}
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(!optionalUser.isPresent()) {
			listChatResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return listChatResponseDTO;
		}
		User user = optionalUser.get();
		
		//---
		
		List<Chat> chats = user.getChats();
		Map<GetChatResponseDTO,Timestamp> mapChats = new LinkedHashMap<GetChatResponseDTO,Timestamp>();
		
		for(Chat chat: chats) {
			List<Message> messages = chat.getMessages();
			Collections.sort(messages);
			Collections.reverse(messages);
			
			Message lastMessage = messages.get(0);
			
			List<User> users = chat.getUsers();
			User otherUser = users.get(0);
			if(otherUser.getId() == user.getId()) otherUser = users.get(1);
			
			GetChatResponseDTO chatResponseDTO = new GetChatResponseDTO();
			chatResponseDTO.setId(chat.getId());
			chatResponseDTO.setIdUser(otherUser.getId());
			chatResponseDTO.setLastMessage(lastMessage.getBody());
			chatResponseDTO.setInputTime(DateUtils.toSimpleString(lastMessage.getDateOfInput()));
			chatResponseDTO.setHasMessageToRead(lastMessage.isToRead());
			chatResponseDTO.setNameChat(otherUser.getUsername());
			
			mapChats.put(chatResponseDTO, lastMessage.getDateOfInput());
		}
		
		List<Map.Entry<GetChatResponseDTO, Timestamp>> entries = new ArrayList<>(mapChats.entrySet());

	    Collections.sort(entries, new Comparator<Map.Entry<GetChatResponseDTO, Timestamp>>() {
	        @Override
	        public int compare(Map.Entry<GetChatResponseDTO, Timestamp> map1, Map.Entry<GetChatResponseDTO, Timestamp> map2) {
	            Timestamp timestamp1 = map1.getValue();
	            Timestamp timestamp2 = map2.getValue();
	        	
	        	return -(timestamp1.compareTo(timestamp2));
	        }
	    });

	    List<GetChatResponseDTO> listChat = new LinkedList<GetChatResponseDTO>();
	    for(Map.Entry<GetChatResponseDTO, Timestamp> entry : entries) {
	    	GetChatResponseDTO tempChat = entry.getKey();
	        listChat.add(tempChat);
	    }
		
	    int numElements = listChat.size();
	    int spacesAvailable = (page+1) * CHAT_PER_PAGE;
	    
	    
	    List<GetChatResponseDTO> pagedChats = null;
	    //TUTTI GLI ELEMENTI NELLA PAGINA
	    if(numElements >= spacesAvailable) {
	    	pagedChats = listChat.subList(page * CHAT_PER_PAGE, (page + 1) * CHAT_PER_PAGE);
	    }
	    //ALCUNI ELEMENTI NELLA PAGINA
	    else if(numElements > spacesAvailable - CHAT_PER_PAGE) {
	    	pagedChats = listChat.subList(page * CHAT_PER_PAGE, numElements);
	    }
	    //NESSUN ELEMENTO NELLA PAGINA
	    else {
	    	pagedChats = new ArrayList<GetChatResponseDTO>();
	    }
	    
		listChatResponseDTO.setListChat(pagedChats);
		listChatResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
		
		return listChatResponseDTO;
	}
	
	public GetIdChatResponseDTO findChatByIdsUser(long idUser1, long idUser2) {
		GetIdChatResponseDTO idChatResponseDTO = new GetIdChatResponseDTO();
		
		if(idUser1 < 0 || idUser2 < 0) {
			idChatResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST);
			return idChatResponseDTO;
		}
		
		Optional<User> optionalUser1 = userRepository.findById(idUser1);
		Optional<User> optionalUser2 = userRepository.findById(idUser2);
		if(optionalUser1.isEmpty() || optionalUser2.isEmpty()) {
			idChatResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return idChatResponseDTO;
		}
		User user1 = optionalUser1.get();
		User user2 = optionalUser2.get();
		
		List<Chat> chats1 = user1.getChats();
		List<Chat> chats2 = user2.getChats();
		
		List<Chat> intersection = new LinkedList<Chat>();
		intersection.addAll(chats1);
		intersection.retainAll(chats2);
		
		if(intersection.isEmpty()) {
			idChatResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return idChatResponseDTO;
		}
		
		Chat chat = intersection.get(0);
		
		idChatResponseDTO = toIdChatResponseDTO(chat);
		
		return idChatResponseDTO;
	}

	public GetListChatMessageResponseDTO findMessagesByIdsUser(long idUser1, long idUser2, Integer page) {
		GetListChatMessageResponseDTO listMessageResponseDTO = new GetListChatMessageResponseDTO();
		
		GetIdChatResponseDTO idChatResponseDTO = findChatByIdsUser(idUser1, idUser2);
		if(!ResultMessageUtils.isSuccess(idChatResponseDTO.getResultMessage())) {
			listMessageResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return listMessageResponseDTO;
		}
		
		listMessageResponseDTO = findMessagesByIdChat(idChatResponseDTO.getId(), page);
		
		return listMessageResponseDTO;
	}
	
	
	public ResultMessageDTO readAllMessage(long idUser1, long idUser2) {
		if(idUser1 < 0 || idUser2 < 0) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		Optional<User> optionalUser1 = userRepository.findById(idUser1);
		Optional<User> optionalUser2 = userRepository.findById(idUser2);
		if(optionalUser1.isEmpty() || optionalUser2.isEmpty()) {
			return ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND;
		}
		
		User user1 = optionalUser1.get();
		User user2 = optionalUser2.get();
		
		List<Chat> chats1 = user1.getChats();
		List<Chat> chats2 = user2.getChats();
		
		List<Chat> intersection = new LinkedList<Chat>();
		intersection.addAll(chats1);
		intersection.retainAll(chats2);
		
		if(intersection.isEmpty()) {
			return ResultMessageUtils.SUCCESS_MESSAGE;
		}
		
		Chat chat = intersection.get(0);
		
		List<Message> listMessage = chat.getMessages();
		User sender = null;
		for(Message message : listMessage) {
			sender = message.getUser();
			if(message.isToRead() && sender.getId() != idUser1) {
				message.setToRead(false);
			}
		}
		
		chat.setMessages(listMessage);
		
		chatRepository.save(chat);
		
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
	
	public HasMessageToReadResponseDTO checkHasMessageToReadByIdUser(long idUser) {
		HasMessageToReadResponseDTO hasMessageToReadResponseDTO = new HasMessageToReadResponseDTO();
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(optionalUser.isEmpty() ) {
			hasMessageToReadResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return hasMessageToReadResponseDTO;
		}
		User user = optionalUser.get();
		
		List<Chat> listChat = user.getChats();
		
		List<Message> tempListMessage = null;
		for(Chat chat: listChat) {
			tempListMessage = chat.getMessages();
			Message tempMessage = null;
			for(int i = tempListMessage.size()-1; i >= 0; i--) {
				tempMessage = tempListMessage.get(i);
				User sender = tempMessage.getUser();
				if(sender.getId() != idUser) {
					if(tempMessage.isToRead()) {
						hasMessageToReadResponseDTO.setHasMessageToRead(true);
						hasMessageToReadResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
						return hasMessageToReadResponseDTO;
					}
					break;
				}
			}
		}
		
		hasMessageToReadResponseDTO.setHasMessageToRead(false);
		hasMessageToReadResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
		return hasMessageToReadResponseDTO;
	}
	
	public ResultMessageDTO addChat(long idUser1, long idUser2) {
		Chat chat = new Chat();
		
		Optional<User> user1 = userRepository.findById(idUser1);
		if(user1.isEmpty()) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		Optional<User> user2 = userRepository.findById(idUser2);
		if(user2.isEmpty()) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		
		GetIdChatResponseDTO getIdChatResponseDTO = findChatByIdsUser(idUser1, idUser2);
		if(ResultMessageUtils.isSuccess(getIdChatResponseDTO.getResultMessage())) {
			return ResultMessageUtils.ERROR_MESSAGE_UNIQUE_VIOLATION;
		}
		ResultMessageDTO resultMessage = getIdChatResponseDTO.getResultMessage();
		if(resultMessage.getCode() != ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND.getCode()){
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		List<User> listUser = new ArrayList<User>();
		listUser.add(user1.get());
		listUser.add(user2.get());
		
		List<Message> listMessage = new ArrayList<Message>();
		
		
		chat.setUsers(listUser);
		chat.setMessages(listMessage);
		
		chatRepository.save(chat);
		
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
	
	
	
	public ResultMessageDTO addConnection(ChatRequestDTO chatRequestDTO) {
		if(!isValid(chatRequestDTO)) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		try {
			chatConnectionRepository.add(chatRequestDTO.getIdConnection());
		}
		catch(Exception e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
			
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
	
	
	public ResultMessageDTO initConnection(ChatRequestDTO chatRequestDTO) {		
		if(!isValid(chatRequestDTO)) {
			System.out.println("Not valid");
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		if(!actionIsInit(chatRequestDTO)) {
			System.out.println("Not init");
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		String idConnection = chatRequestDTO.getIdConnection();
		Map<String, String> payload = chatRequestDTO.getPayload();
		
		String idUser = payload.get(KEY_ID_USER);
		
		//TODO vedi se in dynamo è già presente una connessione col relativo id
		//se è presente rimuovila.
		ChatConnection oldUserConnection = null;
		try {
			oldUserConnection = chatConnectionRepository.findByIdUser(idUser);
		}
		catch(Exception e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		if(oldUserConnection != null) {
			try {
				chatConnectionRepository.delete(oldUserConnection.getIdConnection());
			}
			catch(Exception e) {
				return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
			}
		}
		
		try {
			chatConnectionRepository.updateWithIdUser(idConnection, idUser);
		}
		catch(Exception e) {
			System.out.println("error with update");
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}

		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
	
	
	
	
	public ResultMessageDTO sendMessage(ChatRequestDTO chatRequestDTO) {
		
		if(!isValid(chatRequestDTO)) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		if(!actionIsSendMessage(chatRequestDTO)) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		String idConnection = chatRequestDTO.getIdConnection();
		Map<String,String> payload = chatRequestDTO.getPayload();
		
		String stringIdUser = payload.get(KEY_ID_USER);
		String payloadMessage = payload.get(KEY_MESSAGE);
		String stringInputTime = payload.get(KEY_INPUT_TIME);
		
		System.out.println("idUser: " + stringIdUser + " | message: " + payloadMessage + " | inputTime: " + stringInputTime);
		
		
		long idUserDestination = Long.valueOf(stringIdUser);
		Timestamp inputTime;
		try {
			inputTime = DateUtils.toTimestamp(stringInputTime);
		}
		catch (ParseException e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		
		ChatConnection chatConnection = null;
		
		try {
			chatConnection = chatConnectionRepository.findById(idConnection);
		}
		catch(Exception e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		if(chatConnection == null) {
			return ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND;
		}
		
		System.out.println("|" + chatConnection.getIdConnection() + "| |"+chatConnection.getIdUser()+"|" );
		
		if(chatConnection.getIdUser() == null) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		long idUserSource = Long.valueOf(chatConnection.getIdUser());
		
		if(idUserSource == idUserDestination) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;	
		}
		
		
		System.out.println("source: " + idUserSource + ", destination: " + idUserDestination);
		Optional<User> optionalUserSource = userRepository.findById(idUserSource);
		Optional<User> optionalUserDestination = userRepository.findById(idUserDestination);
		
		if(optionalUserSource.isEmpty() || optionalUserDestination.isEmpty()) {
			return ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND;
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
			//tempChat.setMessages(new ArrayList<Message>());
			List<User> users = new ArrayList<User>();
			users.add(userSource);
			users.add(userDestination);
			
			tempChat.setUsers(users);
			
			chat = chatRepository.save(tempChat);
			
			
			chats1.add(chat);
			chats2.add(chat);
			
			userSource.setChats(chats1);
			userDestination.setChats(chats2);
			
			userRepository.save(userSource);
			userRepository.save(userDestination);
			 
			
		}
		else chat = intersection.get(0);
		
		
		Message message = new Message();
		
		message.setUser(userSource);
		message.setChat(chat);
		message.setBody(payloadMessage);
		message.setDateOfInput(inputTime);
		message.setToRead(true);
		
		messageRepository.save(message);
		
		ChatConnection destinationUserConnection = null;
		try {
			destinationUserConnection = chatConnectionRepository.findByIdUser(stringIdUser);
		}
		catch(Exception e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		//l'utente non è attualmente connessio
		if(destinationUserConnection == null) {
			return ResultMessageUtils.SUCCESS_MESSAGE;
		}
		
		
		
		String idConnectionDestination = destinationUserConnection.getIdConnection();
		//String jsonMessage = "{ \"idUserSource\": \""+ idUserSource +"\", \"message\": \""+ payloadMessage +"\" }";
		
		SendMessageRequestDTO sendMessageRequestDTO = new SendMessageRequestDTO();
		sendMessageRequestDTO.setIdConnectionDestination(idConnectionDestination);
		sendMessageRequestDTO.setIdUserSource(idUserSource);
		sendMessageRequestDTO.setMessage(payloadMessage);
		sendMessageRequestDTO.setInputTime(stringInputTime);
		
		
		ResultMessageDTO sendMessage_resultMessageDTO = messageDAO.sendMessage(sendMessageRequestDTO);
		
		return sendMessage_resultMessageDTO;
	}
	


	public ResultMessageDTO removeConnection(ChatRequestDTO chatRequestDTO) {
		
		if(!isValid(chatRequestDTO)) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		try {
			chatConnectionRepository.delete(chatRequestDTO.getIdConnection());
		}
		catch(Exception e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
			
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
	
	
	

	public GetChatMessageResponseDTO toMessageResponseDTO(Message message){
		if(message == null) return null;
		
		GetChatMessageResponseDTO dto = new GetChatMessageResponseDTO();
		dto.setId(message.getId());
		
		Date dateOfInput = new Date(message.getDateOfInput().getTime());
		DateFormat dateFormat = new SimpleDateFormat();
		String stringDate = dateFormat.format(dateOfInput);
		dto.setDateOfInput(stringDate);
		
		dto.setToRead(message.isToRead());
		
		dto.setBody(message.getBody());
		
		dto.setIdUser(message.getUser().getId());
		dto.setIdChat(message.getChat().getId());
	
		return dto;
	}
	
	public GetListChatMessageResponseDTO toListMessageResponseDTO(long idChat, List<Message> messages) {
		if(messages == null) return null;
		
		List<GetChatMessageResponseDTO> dto = new LinkedList<GetChatMessageResponseDTO>();
		for(Message message : messages) {
			dto.add(toMessageResponseDTO(message));
		}
		
		GetListChatMessageResponseDTO listMessageResponseDTO = new GetListChatMessageResponseDTO();
		listMessageResponseDTO.setListMessage(dto);
		listMessageResponseDTO.setIdChat(idChat);
		listMessageResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
		
		return listMessageResponseDTO;
	}
	
	public GetIdChatResponseDTO toIdChatResponseDTO(Chat chat) {
		if(chat == null) return null;
		
		GetIdChatResponseDTO dto = new GetIdChatResponseDTO();
		dto.setId(chat.getId());
		dto.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);	
		
		return dto;
		
	}


	public boolean isValid(ChatRequestDTO chatRequestDTO) {
		if(chatRequestDTO == null) return false;

		String connectionId = chatRequestDTO.getIdConnection();
		if(connectionId == null || connectionId.isEmpty()) return false;


		Map<String, String> payload = chatRequestDTO.getPayload();
		if(payload == null) return true;
		
		if(!payload.containsKey(KEY_ACTION)) return false;

		return true;
	}

	
	
	public boolean actionIsInit(ChatRequestDTO chatRequestDTO) {

		Map<String, String> payload = chatRequestDTO.getPayload();
		if(payload == null)return false;

		if(payload.isEmpty()) return false; 

		if(!payload.containsKey(KEY_ACTION)) return false;

		String action = payload.get(KEY_ACTION);

		if(action.equals(VALUE_ACTION_INIT_CONNECTION)) {
			if(payload.size() != 2) return false;
			if(!payload.containsKey(KEY_ID_USER)) return false;
			String idUser = payload.get(KEY_ID_USER);
			if(idUser == null || idUser.isEmpty()) return false;
		}
		
		return true;
	}
	
	public boolean actionIsSendMessage(ChatRequestDTO chatRequestDTO) {
		Map<String, String> payload = chatRequestDTO.getPayload();
		if(payload == null || payload.isEmpty()) return false;
		
		if(!payload.containsKey(KEY_ACTION)) return false;

		String action = payload.get(KEY_ACTION);
		
		if(action.equals(VALUE_ACTION_SEND_MESSAGE)) {
			if(payload.size() != 4) return false;
			
			if(!payload.containsKey(KEY_ID_USER)) return false;
			String idUser = payload.get(KEY_ID_USER);
			if(idUser == null || idUser.isEmpty()) return false;

			if(!payload.containsKey(KEY_MESSAGE)) return false;			
			String message = payload.get(KEY_MESSAGE);
			if(message == null || message.isEmpty()) return false;
		
			if(!payload.containsKey(KEY_INPUT_TIME)) return false;
			String inputTime = payload.get(KEY_INPUT_TIME);
			if(inputTime == null || inputTime.isBlank()) return false;
		}
		
		return true;
	}









	




	






}
