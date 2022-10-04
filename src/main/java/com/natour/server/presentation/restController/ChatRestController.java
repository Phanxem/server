package com.natour.server.presentation.restController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.natour.server.application.dtos.request.ChatRequestDTO;
import com.natour.server.application.dtos.response.MessageResponseDTO;
import com.natour.server.application.services.ChatService;
import com.natour.server.application.services.RouteService;
import com.natour.server.data.entities.ChatConnection;

@RestController
@RequestMapping(value="/chat")
public class ChatRestController {

	/*
	
	@Autowired
	private ChatService chatService;
	
	
	//GETs
	@RequestMapping(value="/connect", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<MessageDTO> connect(){
		System.out.println("TEST: Connect");
			
		MessageDTO result = chatService.addConnection();
		
		return new ResponseEntity<MessageDTO>(result, HttpStatus.OK);		
	}
	
	
	@RequestMapping(value="/default", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<MessageDTO> defaultRoute(){
		System.out.println("TEST: Default");
					
		MessageDTO result = new MessageDTO(999,"Unknown");
					
		return new ResponseEntity<MessageDTO>(result, HttpStatus.BAD_REQUEST);		
	}
	
	
	//PUT
	@RequestMapping(value="/initConnection", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<MessageDTO> updateConnection(@RequestBody ChatRequestDTO chatRequestDTO){
		System.out.println("TEST: initConnection");
								
		MessageDTO result = chatService.initConnection(chatRequestDTO);
		
		return new ResponseEntity<MessageDTO>(result, HttpStatus.OK);		
	}
	

	//POSTs
	@RequestMapping(value="/sendMessage", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<MessageDTO> sendMessage(@RequestBody ChatRequestDTO chatRequestDTO){
		System.out.println("TEST: SendMessage");
						
		MessageDTO result = chatService.sendMessage(chatRequestDTO);
						
		return new ResponseEntity<MessageDTO>(result, HttpStatus.OK);		
	}
	
	
	
	//DELETESs
	@RequestMapping(value="/disconnect", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<MessageDTO> disconnectFromChat(@RequestBody ChatRequestDTO chatRequestDTO){
		System.out.println("TEST: Disconnect");
				
		MessageDTO result = chatService.removeConnection(chatRequestDTO);
				
		return new ResponseEntity<MessageDTO>(result, HttpStatus.OK);		
	}
	

	
	*/

	
}
