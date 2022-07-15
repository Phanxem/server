package com.natour.server.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.natour.server.data.dtos.ResponseDTO;
import com.natour.server.data.dtos.UserDTO;
import com.natour.server.data.entities.User;
import com.natour.server.data.repository.FileSystemRepository;
import com.natour.server.data.repository.UserRepository;


@Service
public class UserService {

	private static final String MESSAGE_ADD_USER_OK = "Utente inserito";
	private static final String MESSAGE_UPDATE_IMAGE_OK = "Immagine di Profilo aggiornata";
	
	private static final String MESSAGE_ERROR = "Errore";
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FileSystemRepository fileSystemRepository;

	
	public ResponseDTO addUser(String username) {
		User user = new User(username);
		User result = userRepository.save(user);
		
		if(result == null) return new ResponseDTO(MESSAGE_ADD_USER_OK, HttpStatus.OK);
		return new ResponseDTO(MESSAGE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	public ResponseDTO updateProfileImage(String username, byte[] image) {
		
		try {
			String imageUrl = fileSystemRepository.save(username, image);
			userRepository.updateProfileImageURL(username , imageUrl);
			
			return new ResponseDTO(MESSAGE_UPDATE_IMAGE_OK, HttpStatus.OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			
			return new ResponseDTO(MESSAGE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	public UserDTO getUser(String username) {
		User user = userRepository.findByUsername(username);
		return toUserDTO(user);
	}
	
	public User getUser2(String username) {
		User user = userRepository.findByUsername(username);
		return user;
	}
	
	
	
	public Object updateUserWithProfileImage0(String username, String profileImageURL) {
		userRepository.updateProfileImageURL(username , profileImageURL);
		
		return null;
	}
	
	
	
	
	public FileSystemResource getProfileImage(String profileImageURL) {
		FileSystemResource profileImage = fileSystemRepository.findInFileSystem(profileImageURL);
		
		return profileImage;
	}
	
	
	
	//MAPPER
	
	public UserDTO toUserDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setGender(user.getGender());
		userDTO.setDateOfBirth(user.getDateOfBirth());
		userDTO.setPlaceOfResidence(user.getPlaceOfResidence());
		userDTO.setUsername(user.getUsername());
		
		FileSystemResource image = getProfileImage(user.getProfileImageURL());
		userDTO.setProfileImage(image);
		
		return userDTO;
	}
}
