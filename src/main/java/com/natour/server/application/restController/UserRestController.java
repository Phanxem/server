package com.natour.server.application.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.natour.server.application.services.UserService;
import com.natour.server.data.dtos.ResponseDTO;
import com.natour.server.data.dtos.UserDTO;
import com.natour.server.data.entities.User;





@RestController
@RequestMapping(value="/user")
public class UserRestController {

	@Autowired
	private UserService userService;

	//	 @RequestParam(defaultValue = "0") Integer page

	
	//ADD USER--------------------------------------------------------------
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResponseDTO> addUser(@RequestParam(required = true) String username){
		System.out.println("TEST: ADD");
		
		ResponseDTO result = userService.addUser(username);
		return new ResponseEntity<ResponseDTO>(result, result.getResultCode());
	}
	
	
	
	//UPDATE PROFILE IMAGE
	@RequestMapping(value="/update/image", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ResponseDTO> updateProfileImage(@RequestParam(required = true) String username, @RequestParam(required = true) byte[] image){
		System.out.println("TEST: UPDATE IMAGE");
		
		ResponseDTO result = userService.updateProfileImage(username, image);
		return new ResponseEntity<ResponseDTO>(result, result.getResultCode());
	}
	
	
	
	
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
	
	
	
	//UPDATE PROFILE IMAGE
		@RequestMapping(value="/update/image0", method=RequestMethod.PUT)
		@ResponseBody
		public ResponseEntity<Object> updateUserProfileImage0(@RequestParam(required = true) String username, @RequestParam(required = true) String imageURL){
			System.out.println("TEST: UPDATE IMAGE 0");
			
			Object result = userService.updateUserWithProfileImage0(username, imageURL);
			return new ResponseEntity<Object>(result, HttpStatus.OK);
		}
	
}
