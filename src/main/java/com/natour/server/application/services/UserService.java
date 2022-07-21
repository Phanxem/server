package com.natour.server.application.services;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import com.natour.server.FileSystemUtils;
import com.natour.server.application.dtos.OptionalInfoUserDTO;
import com.natour.server.application.dtos.UserDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.OptionalInfoUserDTOInvalidException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserOptionalInfoUpdateFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserProfileImageNullException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserProfileImageUpdateFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserProfileImageSaveFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserUsernameNullException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserUsernameUniqueException;
import com.natour.server.data.entities.User;
import com.natour.server.data.repository.FileSystemRepository;
import com.natour.server.data.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FileSystemRepository fileSystemRepository;

	
	//ADDs
	public UserDTO addUser(String username) {
		if(username == null) throw new UserUsernameNullException();
		
		User user = userRepository.findByUsername(username);
		if(user != null) throw new UserUsernameUniqueException();
		
		user = new User(username);
		User result = userRepository.save(user);
		
		return toUserDTO(result);
	}
	
	
	//UPDATEs
	public UserDTO updateProfileImage(String username, byte[] image) {
		
		if(username == null) throw new UserUsernameNullException();
		
		User user = userRepository.findByUsername(username);
		if(user == null) throw new UserNotFoundException();
		
		if(image == null) throw new UserProfileImageNullException();
			
		/*
		//Non necessaria
		if(user.getProfileImageURL() != null) {
			//TODO rimuovi l'immagine dal fileSystem
		}
		*/
		
		String imageUrl;
		try {
			imageUrl = fileSystemRepository.save(username, image);
		}
		catch (IOException e) {
			throw new UserProfileImageSaveFailureException(e);
		}
		
		int updatedElement = userRepository.updateProfileImageURL(username , imageUrl);
		if(updatedElement == 0) throw new UserProfileImageUpdateFailureException();
		
		User result = userRepository.findByUsername(username);
		return toUserDTO(result);
	}
	
	
	public UserDTO updateOptionalInfo(String username, OptionalInfoUserDTO optionalInfoDTO) {
		
		if(username == null) throw new UserUsernameNullException();
		
		if(isValidDTO(optionalInfoDTO)) throw new OptionalInfoUserDTOInvalidException();
		
		User user = userRepository.findByUsername(username);
		if(user == null) throw new UserNotFoundException();
		
		String  placeOfResidence = optionalInfoDTO.getPlaceOfResidence();
		String gender = optionalInfoDTO.getGender();
		Date dateOfBirth = optionalInfoDTO.getDateOfBirth();
		
		Timestamp timestampOfBirth = null;
		if(dateOfBirth != null) timestampOfBirth = new Timestamp(dateOfBirth.getTime());
			
		int updatedElement = userRepository.updateOptionalInfo(username, placeOfResidence, timestampOfBirth, gender);
		if(updatedElement == 0) throw new UserOptionalInfoUpdateFailureException();
		
		User result = userRepository.findByUsername(username);
		return toUserDTO(result);
	}
	
	
	
	//FINDs
	public UserDTO findUserById(long id) {		
		Optional<User> user = userRepository.findById(id);
		//TODO DA TESTARE
		if(!user.isPresent()) throw new UserNotFoundException();
		return toUserDTO(user.get());
	}
	
	public UserDTO findUserByUsername(String username) {
		if(username == null) throw new UserUsernameNullException();
		
		User user = userRepository.findByUsername(username);
		//TODO DA TESTARE
		if(user == null) throw new UserNotFoundException();
		return toUserDTO(user);
	}
	
	
	//SEARCHs
	
	public List<UserDTO> searchUserByUsername(String username){
		if(username == null) throw new UserUsernameNullException();
		
		List<User> users = userRepository.findByUsernameContaining(username);
				
		List<UserDTO> usersDTO = toListUserDTO(users);
		
		return usersDTO;
	}
	
	//REMOVEs
	

	
	
	
	
	//-----------------------------------------------------------------------------------------------

	
	
	
	
	
	//MAPPER
	
	public UserDTO toUserDTO(User user) {
		
		if(user == null) return null;
		
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setGender(user.getGender());
		userDTO.setPlaceOfResidence(user.getPlaceOfResidence());
		userDTO.setUsername(user.getUsername());
		
		Date dateOfBirth = new Date(user.getDateOfBirth().getTime());
		userDTO.setDateOfBirth(dateOfBirth);
		
		if(user.getProfileImageURL() != null) {
			FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(user.getProfileImageURL());
			byte[] profileImage = FileSystemUtils.toArrayByte(fileSystemResource);
			userDTO.setProfileImage(profileImage);
		}
		else userDTO.setProfileImage(null);
		
		return userDTO;
	}
	
	public List<UserDTO> toListUserDTO(List<User> users){
		if(users == null) return null;
		
		List<UserDTO> usersDTO = new LinkedList<UserDTO>();
		for(User user : users) {
			usersDTO.add(toUserDTO(user));
		}
		return usersDTO;
	}
	
	
	
	//VALIDATORs
	public boolean isValidDTO(OptionalInfoUserDTO optionalInfoDTO) {
		if(optionalInfoDTO == null) return false;
		
		return true;
		
	}
}
