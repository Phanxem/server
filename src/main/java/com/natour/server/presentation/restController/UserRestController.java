package com.natour.server.presentation.restController;

import java.sql.Date;
import java.util.List;

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

import com.natour.server.application.dtos.OptionalInfoUserDTO;
import com.natour.server.application.dtos.UserDTO;
import com.natour.server.application.services.UserService;


@RestController
@RequestMapping(value="/user")
public class UserRestController {

	//TODO definire alle api POST e PUT un body (RequestBody) e non parametri (RequestParam)
	
	@Autowired
	private UserService userService;

	//GETs
	@RequestMapping(value="/get/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<UserDTO> getUserById(@PathVariable("id") long id){
		System.out.println("TEST: GET id");
		
		UserDTO result = userService.findUserById(id);
		if(result != null) return new ResponseEntity<UserDTO>(result, HttpStatus.OK);
		return new ResponseEntity<UserDTO>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//---
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<UserDTO> getUserByUsername(@RequestParam(required = true) String username){
		System.out.println("TEST: GET username");
		
		UserDTO result = userService.findUserByUsername(username);
		if(result != null) return new ResponseEntity<UserDTO>(result, HttpStatus.OK);
		return new ResponseEntity<UserDTO>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

	//---
	
	//SEARCH
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<UserDTO>> searchUserByUsername(@RequestParam(required = true) String username){
		System.out.println("TEST: SEARCH");
		
		List<UserDTO> result = userService.searchUserByUsername(username);
		if(result != null) return new ResponseEntity<List<UserDTO>>(result, HttpStatus.OK);
		return new ResponseEntity<List<UserDTO>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	
	
	
	
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<UserDTO> addUser(@RequestBody(required = true) String username){
		System.out.println("TEST: ADD");
		
		UserDTO result = userService.addUser(username);
		
		if(result != null) return new ResponseEntity<UserDTO>(result, HttpStatus.OK);
		return new ResponseEntity<UserDTO>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	
	
	//PUTs
	
	
	@RequestMapping(value="/update/image", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<UserDTO> updateProfileImage(@RequestParam(required = true) String username,
													  @RequestParam(required = true) byte[] image)
	{
		System.out.println("TEST: UPDATE IMAGE");
		
		UserDTO result = userService.updateProfileImage(username, image);
		
		if(result != null) return new ResponseEntity<UserDTO>(result, HttpStatus.OK);
		return new ResponseEntity<UserDTO>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@RequestMapping(value="/update/optional_info", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<UserDTO> updateOptionalInfo(@RequestParam(required = true) String username,
													  @RequestParam(required = false) String placeOfResidence,
													  @RequestParam(required = false) Date dateOfBirth,
													  @RequestParam(required = false) String gender)
	{
		System.out.println("TEST: UPDATE IMAGE");
		OptionalInfoUserDTO optionalInfoDTO = new OptionalInfoUserDTO(placeOfResidence, dateOfBirth, gender);
		UserDTO result = userService.updateOptionalInfo(username, optionalInfoDTO);
		
		if(result != null) return new ResponseEntity<UserDTO>(result, HttpStatus.OK);
		return new ResponseEntity<UserDTO>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//DELETEs
	
	
	
	
	
	
	

	
	
	
	//UPDATE OPTIONAL INFORMATION
	
	
	
	
	
	
	
	
/*	
	
	//GET USER BY USERNAME
	@RequestMapping(value="/get", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<UserDTO> getUser(String username){
		System.out.println("TEST: GET");
		
		UserDTO result = userService.getUser(username);
		return new ResponseEntity<UserDTO>(result, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="/get2", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getUser2(String username){
		System.out.println("TEST: GET");
		
		User result = userService.getUser2(username);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
	
	*/
	
	//UPDATE PROFILE IMAGE
		@RequestMapping(value="/update/image0", method=RequestMethod.PUT)
		@ResponseBody
		public ResponseEntity<Object> updateUserProfileImage0(@RequestParam(required = true) String username, @RequestParam(required = true) String imageURL){
			System.out.println("TEST: UPDATE IMAGE 0");
			
			Object result = userService.updateUserWithProfileImage0(username, imageURL);
			return new ResponseEntity<Object>(result, HttpStatus.OK);
		}
	
}
