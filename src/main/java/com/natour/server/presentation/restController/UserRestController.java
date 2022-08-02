package com.natour.server.presentation.restController;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.natour.server.application.dtos.MessageDTO;
import com.natour.server.application.dtos.SuccessMessageDTO;
import com.natour.server.application.dtos.UserDTO;
import com.natour.server.application.dtos.request.OptionalInfoUserRequestDTO;
import com.natour.server.application.dtos.response.UserResponseDTO;
import com.natour.server.application.services.UserService;


@RestController
@RequestMapping(value="/user")
public class UserRestController {
	
	@Autowired
	private UserService userService;

	//Test
	@RequestMapping(value="/test", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> test(){
		System.out.println("TEST: test");
		
		
		return new ResponseEntity<String>("OK", HttpStatus.OK);
		
	}	
	
	
	//-----------------------------------------------------------------------------
	
	//TODO AGGIORNATA
	@RequestMapping(value="/get/{id}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") long id){
		System.out.println("TEST: GET id");

		//UserDTO result = userService.findUserById(id);
		UserResponseDTO result = userService.findUserById(id);

		return new ResponseEntity<UserResponseDTO>(result, HttpStatus.OK);
	}	
			
	
	//GETs
	@RequestMapping(value="/get/{id}/image", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> getUserImageById(@PathVariable("id") long id, HttpServletRequest request){
		System.out.println("TEST: GET IMAGE id");


		Resource result = userService.findUserImageById(id);

		if(result == null) return ResponseEntity.ok().body(null);
		
		String contentType = null;
        try {
			contentType = request.getServletContext().getMimeType(result.getFile().getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(contentType == null) contentType = "application/octet-stream";
       
		return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.getFilename() + "\"")
                .body(result);
	}
	
	
	
	//---
	//TODO AGGIORNATA
	@RequestMapping(value="/get", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<UserResponseDTO> getUserByUsername(@RequestParam String username){
		System.out.println("TEST: GET username");
		
		UserResponseDTO result = userService.findUserByUsername(username);
		return new ResponseEntity<UserResponseDTO>(result, HttpStatus.OK);
		
	}
	
	
	@RequestMapping(value="/get/image", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> getUserImageById(@RequestParam String username, HttpServletRequest request){
		System.out.println("TEST: GET IMAGE username");

		Resource result = userService.findUserImageByUsername(username);

		if(result == null) return ResponseEntity.ok().body(null);
		
		String contentType = null;
        try {
			contentType = request.getServletContext().getMimeType(result.getFile().getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(contentType == null) contentType = "application/octet-stream";
       
		return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.getFilename() + "\"")
                .body(result);
	}
	
	
	
	
	//SEARCH
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<UserResponseDTO>> searchUserByUsername(@RequestParam String username){
		System.out.println("TEST: SEARCH");
		
		List<UserResponseDTO> result = userService.searchUserByUsername(username);
		return new ResponseEntity<List<UserResponseDTO>>(result, HttpStatus.OK);
		
	}
	
	
	
	
	
	
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<MessageDTO> addUser(@RequestParam String username){
		System.out.println("TEST: ADD");
		
		MessageDTO result = userService.addUser(username);
		
		return new ResponseEntity<MessageDTO>(result, HttpStatus.OK);
		
	}
	
	
	
	
	//PUTs
	//TODO TEST
	@RequestMapping(value="/update/image", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<MessageDTO> updateProfileImage(@RequestParam String username,
													  @RequestBody MultipartFile image)
	{
		System.out.println("TEST: UPDATE IMAGE");
		
		MessageDTO result = userService.updateProfileImage(username, image);
		
		
		return new ResponseEntity<MessageDTO>(result, HttpStatus.OK);
		
	}
	
	//---

	@RequestMapping(value="/update/optionalInfo", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<MessageDTO> updateOptionalInfo(@RequestParam String username,
													     OptionalInfoUserRequestDTO optionalInfo)
	{
		System.out.println("TEST: UPDATE OPTIONAL INFO");
		MessageDTO result = userService.updateOptionalInfo(username, optionalInfo);
		
		return new ResponseEntity<MessageDTO>(result, HttpStatus.OK);
		
	}

	//DELETEs
	
	
	
	
	
	
	


	
	
	
	
	
	
	
	
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
	
	
	
	//UPDATE PROFILE IMAGE
		@RequestMapping(value="/update/image0", method=RequestMethod.PUT)
		@ResponseBody
		public ResponseEntity<Object> updateUserProfileImage0(@RequestParam(required = true) String username, @RequestParam(required = true) String imageURL){
			System.out.println("TEST: UPDATE IMAGE 0");
			
			Object result = userService.updateUserWithProfileImage0(username, imageURL);
			return new ResponseEntity<Object>(result, HttpStatus.OK);
		}
	*/
}
