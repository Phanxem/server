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

import com.natour.server.application.dtos.request.AddUserRequestDTO;
import com.natour.server.application.dtos.request.UpdateUserOptionalInfoRequestDTO;
import com.natour.server.application.dtos.response.ResourceResponseDTO;
import com.natour.server.application.dtos.response.ListUserResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.UserResponseDTO;
import com.natour.server.application.services.ResultCodeUtils;
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
	
	
	//GETs
	@RequestMapping(value="/get/{idUser}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("idUser") long idUser){
		System.out.println("TEST: GET user");

		UserResponseDTO result = userService.findUserById(idUser);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<UserResponseDTO>(result, resultHttpStatus);
	}	
			
	
	@RequestMapping(value="/get/{idUser}/image", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> getUserImageById(@PathVariable("idUser") long idUser, HttpServletRequest request){
		System.out.println("TEST: GET IMAGE id");

		ResourceResponseDTO result = userService.findUserImageById(idUser);

		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus;
		if(resultMessage.getCode() != 200) {
			resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
			return new ResponseEntity<Resource>((Resource) null, resultHttpStatus);
		}
		
		Resource resource = result.getResource();
		String contentType = null;
        try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		}
        catch (IOException e) {
			//TODO
			return new ResponseEntity<Resource>((Resource) null, HttpStatus.NOT_FOUND);
		}
        if(contentType == null) contentType = "application/octet-stream";
        
		return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
	}
	
	
	@RequestMapping(value="/get/identityProvider", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<UserResponseDTO> getUserByIdP(@RequestParam String identityProvider, @RequestParam String idIdentityProvided){
		System.out.println("TEST: GET user");

		UserResponseDTO result = userService.findUserByIdp(identityProvider, idIdentityProvided);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<UserResponseDTO>(result, resultHttpStatus);
	}	

	
	@RequestMapping(value="/get/connection/{idConnection}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<UserResponseDTO> getUserByIdConnection(@PathVariable("idConnection") long idConnection){
		System.out.println("TEST: GET user");

		UserResponseDTO result = userService.findUserById(idConnection);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<UserResponseDTO>(result, resultHttpStatus);
	}
	
	
	
	//SEARCH
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ListUserResponseDTO> searchUserByUsername(@RequestParam String username, @RequestParam(defaultValue = "0") Integer page){
		System.out.println("TEST: SEARCH");
		
		ListUserResponseDTO result = userService.searchUserByUsername(username, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<ListUserResponseDTO>(result, resultHttpStatus);
		
	}
	
	@RequestMapping(value="/get/{idUser}/conversation", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ListUserResponseDTO> searchUserConversation(@PathVariable("idUser") long idUser, @RequestParam(defaultValue = "0") Integer page){
		System.out.println("TEST: GET Conversation");
		
		ListUserResponseDTO result = userService.searchUserWithConversation(idUser, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(resultMessage.getCode());
		
		return new ResponseEntity<ListUserResponseDTO>(result, resultHttpStatus);
		
	}
	
	
	
	
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> addUser(@RequestBody AddUserRequestDTO addUserRequest){
		System.out.println("TEST: ADD");
		
		ResultMessageDTO result = userService.addUser(addUserRequest);
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(result.getCode());
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
		
	}
	
	
	
	
	//PUTs
	//TODO TEST
	@RequestMapping(value="/update/{idUser}/image", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> updateProfileImage(@PathVariable("idUser") long idUser,
															   @RequestBody MultipartFile image)
	{
		System.out.println("TEST: UPDATE IMAGE");
		
		ResultMessageDTO result = userService.updateProfileImage(idUser, image);
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(result.getCode());
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
	}
	
	//---

	@RequestMapping(value="/update/{idUser}/optionalInfo", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> updateOptionalInfo(@PathVariable("idUser") long idUser,
															   @RequestBody UpdateUserOptionalInfoRequestDTO optionalInfo)
	{
		System.out.println("TEST: UPDATE OPTIONAL INFO");
		ResultMessageDTO result = userService.updateOptionalInfo(idUser, optionalInfo);
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(result.getCode());
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
		
	}

	//DELETEs
	//GETs
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> deleteUserById(@RequestParam String idIdentityProvided){
			System.out.println("TEST: DELETE user");

			ResultMessageDTO result = userService.deleteCognitoUser(idIdentityProvided);
			HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(result.getCode());
			
			return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
		}	

}
