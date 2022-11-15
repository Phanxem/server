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
import com.natour.server.application.dtos.request.SaveUserRequestDTO;
import com.natour.server.application.dtos.request.ChatRequestDTO;
import com.natour.server.application.dtos.response.GetIdChatResponseDTO;
import com.natour.server.application.dtos.response.GetListChatResponseDTO;
import com.natour.server.application.dtos.response.GetListChatMessageResponseDTO;
import com.natour.server.application.dtos.response.GetListUserResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.GetUserResponseDTO;
import com.natour.server.application.dtos.response.HasMessageToReadResponseDTO;
import com.natour.server.application.services.ChatService;
import com.natour.server.application.services.RouteService;
import com.natour.server.application.services.utils.ResultMessageUtils;
import com.natour.server.data.entities.dynamoDB.ChatConnection;

@RestController
@RequestMapping(value="/chat")
public class ChatRestController {

	@Autowired
	private ChatService chatService;
	

	//GETs	
	@RequestMapping(value="/get/{idChat}/messages", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetListChatMessageResponseDTO> getMessagesByIdChat(@PathVariable("idChat") long idChat, @RequestParam(defaultValue = "0") Integer page){
		System.out.println("TEST: GET user");
		
		GetListChatMessageResponseDTO result = chatService.findMessagesByIdChat(idChat, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
			
		return new ResponseEntity<GetListChatMessageResponseDTO>(result, resultHttpStatus);
	}	
	
	@RequestMapping(value="/get/users", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetIdChatResponseDTO> getChatByIdsUser(@RequestParam long idUser1, @RequestParam long idUser2){
		System.out.println("TEST: GET user");
		
		GetIdChatResponseDTO result = chatService.findChatByIdsUser(idUser1, idUser2);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
				
		return new ResponseEntity<GetIdChatResponseDTO>(result, resultHttpStatus);
	}	
	
	@RequestMapping(value="/get/messages", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetListChatMessageResponseDTO> getMessagesByIdsUser(@RequestParam long idUser1, @RequestParam long idUser2, @RequestParam(defaultValue = "0") Integer page){
		System.out.println("TEST: GET MESSAGES BY users");
		
		GetListChatMessageResponseDTO result = chatService.findMessagesByIdsUser(idUser1, idUser2, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
			
		return new ResponseEntity<GetListChatMessageResponseDTO>(result, resultHttpStatus);
	}
	
	@RequestMapping(value="/get/user/{idUser}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetListChatResponseDTO> searchConversation(@PathVariable("idUser") long idUser, @RequestParam(defaultValue = "0") Integer page){
		System.out.println("TEST: GET Conversation");
		
		GetListChatResponseDTO result = chatService.searchByIdUser(idUser, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetListChatResponseDTO>(result, resultHttpStatus);
	}
	
	@RequestMapping(value="/has/messageToRead/{idUser}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<HasMessageToReadResponseDTO> hasMessageToRead(@PathVariable("idUser") long idUser){
		System.out.println("TEST: HAS messageToRead");
		
		HasMessageToReadResponseDTO result = chatService.checkHasMessageToReadByIdUser(idUser);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		System.out.println("has result: " + result.isHasMessageToRead());
		
		return new ResponseEntity<HasMessageToReadResponseDTO>(result, resultHttpStatus);
	}
	
	
	@RequestMapping(value="/readAllMessage", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> readAllMessageByIdsUser(@RequestParam long idUser1, @RequestParam long idUser2){
		System.out.println("TEST: ReadAllMessage");
			
		ResultMessageDTO result = chatService.readAllMessage(idUser1, idUser2);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);		
	}
	
	
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> addChat(@RequestParam long idUser1, @RequestParam long idUser2){
		System.out.println("TEST: addChat");
							
		ResultMessageDTO result = chatService.addChat(idUser1, idUser2);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
							
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);		
	}
	
	

	

	
	
		
	
	//WEBSOCKET
	
	
	@RequestMapping(value="/default", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> defaultRoute(){
		System.out.println("TEST: Default");
					
		
					
		return new ResponseEntity<ResultMessageDTO>(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST, HttpStatus.BAD_REQUEST);		
	}
	
	
	//POSTs
	@RequestMapping(value="/connect", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> connect(@RequestBody ChatRequestDTO chatRequestDTO){
		System.out.println("TEST: Connect");
			
		ResultMessageDTO result = chatService.addConnection(chatRequestDTO);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);		
	}
	
	@RequestMapping(value="/sendMessage", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> sendMessage(@RequestBody ChatRequestDTO chatRequestDTO){
		System.out.println("TEST: SendMessage");
							
		ResultMessageDTO result = chatService.sendMessage(chatRequestDTO);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
							
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);		
	}
	

	//PUT
	@RequestMapping(value="/initConnection", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> updateConnection(@RequestBody ChatRequestDTO chatRequestDTO){
		System.out.println("TEST: initConnection");
									
		ResultMessageDTO result = chatService.initConnection(chatRequestDTO);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
			
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);		
	}
		
	
	//DELETESs
	@RequestMapping(value="/disconnect", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> disconnectFromChat(@RequestBody ChatRequestDTO chatRequestDTO){
		System.out.println("TEST: Disconnect");
				
		ResultMessageDTO result = chatService.removeConnection(chatRequestDTO);
				
		return new ResponseEntity<ResultMessageDTO>(result, HttpStatus.OK);		
	}
}
