package com.natour.server.presentation.restController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.natour.server.application.dtos.request.AddUserRequestDTO;
import com.natour.server.application.dtos.request.ChatRequestDTO;
import com.natour.server.application.dtos.response.IdChatResponseDTO;
import com.natour.server.application.dtos.response.ListChatResponseDTO;
import com.natour.server.application.dtos.response.ListMessageResponseDTO;
import com.natour.server.application.dtos.response.ListUserResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.UserResponseDTO;
import com.natour.server.application.services.ChatService;
import com.natour.server.application.services.ResultCodeUtils;
import com.natour.server.application.services.RouteService;
import com.natour.server.data.entities.dynamoDB.ChatConnection;

@RestController
@RequestMapping(value="/chat")
public class ChatRestController {

	@Autowired
	private ChatService chatService;
	
	
	

	
	
	
	//GETs	
	@RequestMapping(value="/get/{idChat}/messages", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ListMessageResponseDTO> getMessagesByIdChat(@PathVariable("idChat") long idChat, @RequestParam(defaultValue = "0") Integer page){
		System.out.println("TEST: GET user");
		
		ListMessageResponseDTO result = chatService.findMessagesByIdChat(idChat, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
			
		return new ResponseEntity<ListMessageResponseDTO>(result, resultHttpStatus);
	}	
	
	@RequestMapping(value="/get/users", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<IdChatResponseDTO> getChatByIdsUser(@RequestParam long idUser1, @RequestParam long idUser2){
		System.out.println("TEST: GET user");
		
		IdChatResponseDTO result = chatService.findChatByIdsUser(idUser1, idUser2);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
				
		return new ResponseEntity<IdChatResponseDTO>(result, resultHttpStatus);
	}	
	
	//TODO
	@RequestMapping(value="/connect", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> connect(@RequestBody ChatRequestDTO chatRequestDTO){
		System.out.println("TEST: Connect");
			
		ResultMessageDTO result = chatService.addConnection(chatRequestDTO);
		
		return new ResponseEntity<ResultMessageDTO>(result, HttpStatus.OK);		
	}
	
	//TODO
	@RequestMapping(value="/default", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> defaultRoute(){
		System.out.println("TEST: Default");
					
		ResultMessageDTO result = new ResultMessageDTO(999,"Unknown");
					
		return new ResponseEntity<ResultMessageDTO>(result, HttpStatus.BAD_REQUEST);		
	}
	
	
	//SEARCHs
	@RequestMapping(value="/get/user/{idUser}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ListChatResponseDTO> searchConversation(@PathVariable("idUser") long idUser, @RequestParam(defaultValue = "0") Integer page){
		System.out.println("TEST: GET Conversation");
		
		ListChatResponseDTO result = chatService.searchByIdUser(idUser, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<ListChatResponseDTO>(result, resultHttpStatus);
		
	}
	
	
	
	//POSTs
	//TODO
	@RequestMapping(value="/sendMessage", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> sendMessage(@RequestBody ChatRequestDTO chatRequestDTO){
		System.out.println("TEST: SendMessage");
							
		ResultMessageDTO result = chatService.sendMessage(chatRequestDTO);
							
		return new ResponseEntity<ResultMessageDTO>(result, HttpStatus.OK);		
	}
	
	
	
	//PUT
	//TODO
	@RequestMapping(value="/initConnection", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> updateConnection(@RequestBody ChatRequestDTO chatRequestDTO){
		System.out.println("TEST: initConnection");
								
		ResultMessageDTO result = chatService.initConnection(chatRequestDTO);
		
		return new ResponseEntity<ResultMessageDTO>(result, HttpStatus.OK);		
	}
	
	
	//DELETESs
	//TODO
	@RequestMapping(value="/disconnect", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> disconnectFromChat(@RequestBody ChatRequestDTO chatRequestDTO){
		System.out.println("TEST: Disconnect");
				
		ResultMessageDTO result = chatService.removeConnection(chatRequestDTO);
				
		return new ResponseEntity<ResultMessageDTO>(result, HttpStatus.OK);		
	}
		
}
