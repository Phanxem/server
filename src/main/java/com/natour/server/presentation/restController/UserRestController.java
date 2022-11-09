package com.natour.server.presentation.restController;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.natour.server.application.dtos.request.SaveUserRequestDTO;
import com.natour.server.application.dtos.request.SaveUserImageRequestDTO;
import com.natour.server.application.dtos.request.SaveUserOptionalInfoRequestDTO;
import com.natour.server.application.dtos.response.GetResourceResponseDTO;
import com.natour.server.application.dtos.response.GetListUserResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.GetUserResponseDTO;
import com.natour.server.application.services.UserService;
import com.natour.server.application.services.utils.ResultMessageUtils;


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
	public ResponseEntity<GetUserResponseDTO> getUserById(@PathVariable("idUser") long idUser){
		System.out.println("TEST: GET user");

		GetUserResponseDTO result = userService.findUserById(idUser);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetUserResponseDTO>(result, resultHttpStatus);
	}	
			
	
	@RequestMapping(value="/get/{idUser}/image", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> getUserImageById(@PathVariable("idUser") long idUser, HttpServletRequest request){
		System.out.println("TEST: GET IMAGE id");

		GetResourceResponseDTO result = userService.findUserImageById(idUser);
		
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus;
		
		if(resultMessage.getCode() != 200) {
			resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
			//InputStreamResource inputStreamResource = new InputStreamResource(null);
			Resource resource = new InputStreamResource(null);
			return new ResponseEntity<Resource>(resource, resultHttpStatus);
		}
		
		Resource resource = result.getResource();
		String contentType = null;
        /*
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		}
        catch (IOException e) {
			//TODO
			return new ResponseEntity<Resource>((Resource) null, HttpStatus.NOT_FOUND);
		}
        */
        if(contentType == null) contentType = "application/octet-stream";
        
        
        
        if(resource == null) {
        	resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
			return new ResponseEntity<Resource>(resource, resultHttpStatus);
        }
        
		return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
	}
	
	
	@RequestMapping(value="/get/identityProvider", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetUserResponseDTO> getUserByIdP(@RequestParam String identityProvider, @RequestParam String idIdentityProvided){
		System.out.println("TEST: GET user");

		GetUserResponseDTO result = userService.findUserByIdp(identityProvider, idIdentityProvided);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetUserResponseDTO>(result, resultHttpStatus);
	}	

	
	@RequestMapping(value="/get/connection/{idConnection}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetUserResponseDTO> getUserByIdConnection(@PathVariable("idConnection") String idConnection){
		System.out.println("TEST: GET user connection");

		GetUserResponseDTO result = userService.findUserByIdConnection(idConnection);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetUserResponseDTO>(result, resultHttpStatus);
	}
	
	
	
	//SEARCH
	@RequestMapping(value="/search", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetListUserResponseDTO> searchUserByUsername(@RequestParam String username, @RequestParam(defaultValue = "0") Integer page){
		System.out.println("TEST: SEARCH");
		
		GetListUserResponseDTO result = userService.searchUserByUsername(username, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetListUserResponseDTO>(result, resultHttpStatus);
		
	}
	
	@RequestMapping(value="/get/{idUser}/conversation", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<GetListUserResponseDTO> searchUserConversation(@PathVariable("idUser") long idUser, @RequestParam(defaultValue = "0") Integer page){
		System.out.println("TEST: GET Conversation");
		
		GetListUserResponseDTO result = userService.searchUserWithConversation(idUser, page);
		ResultMessageDTO resultMessage = result.getResultMessage();
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(resultMessage);
		
		return new ResponseEntity<GetListUserResponseDTO>(result, resultHttpStatus);
		
	}
	
	
	
	
	//POSTs
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> addUser(SaveUserRequestDTO addUserRequest){
		System.out.println("TEST: ADD");
		
		ResultMessageDTO result = userService.addUser(addUserRequest);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
		
	}
	
	
	
	
	//PUTs
	//TODO TEST
	@RequestMapping(value="/update/{idUser}/image", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> updateProfileImage(@PathVariable("idUser") long idUser,
															   @ModelAttribute SaveUserImageRequestDTO imageRequest)
	{
		System.out.println("TEST: UPDATE IMAGE");
		
		ResultMessageDTO result = userService.updateProfileImage(idUser, imageRequest);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
	}
	
	//---

	@RequestMapping(value="/update/{idUser}/optionalInfo", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> updateOptionalInfo(@PathVariable("idUser") long idUser,
															   SaveUserOptionalInfoRequestDTO optionalInfo)
	{
		System.out.println("TEST: UPDATE OPTIONAL INFO");
		ResultMessageDTO result = userService.updateOptionalInfo(idUser, optionalInfo);
		HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
		
	}

/*
	@RequestMapping(value="/update/{idUser}/linkTo/Facebook", method=RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> linkToFacebook(@PathVariable("idUser") long idUser)
	{
		System.out.println("TEST: LINK TO FACEBOOK");
		ResultMessageDTO result = userService.linkToFacebook(idUser);
		HttpStatus resultHttpStatus = ResultCodeUtils.toHttpStatus(result.getCode());
		
		return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
		
	}
	*/
	
	
	//DELETEs
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<ResultMessageDTO> deleteUserById(@RequestParam String idIdentityProvided){
			System.out.println("TEST: DELETE user");

			ResultMessageDTO result = userService.deleteCognitoUser(idIdentityProvided);
			HttpStatus resultHttpStatus = ResultMessageUtils.toHttpStatus(result);
			
			return new ResponseEntity<ResultMessageDTO>(result, resultHttpStatus);
		}	

}
