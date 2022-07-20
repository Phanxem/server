package com.natour.server.application.services;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import com.natour.server.application.dtos.OptionalInfoUserDTO;
import com.natour.server.application.dtos.UserDTO;
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
		User user = new User(username);
		User result = userRepository.save(user);
		
		return toUserDTO(result);
	}
	
	
	//UPDATEs
	public UserDTO updateProfileImage(String username, byte[] image) {
		
		try {
			String imageUrl = fileSystemRepository.save(username, image);
			userRepository.updateProfileImageURL(username , imageUrl);
			User result = userRepository.findByUsername(username);
			
			return toUserDTO(result);
		}
		catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public UserDTO updateOptionalInfo(String username, OptionalInfoUserDTO optionalInfoDTO) {
		
		try {
			String  placeOfResidence = optionalInfoDTO.getPlaceOfResidence();
			Date dateOfBirth = optionalInfoDTO.getDateOfBirth();
			String gender = optionalInfoDTO.getGender();
			
			userRepository.updateOptionalInfo(username, placeOfResidence, dateOfBirth, gender);
			User result = userRepository.findByUsername(username);
			
			return toUserDTO(result);
		}
		catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	//FINDs
	public UserDTO findUserById(long id) {
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) return toUserDTO(user.get());
		return null;
	}
	
	public UserDTO findUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
		return toUserDTO(user);
	}
	
	
	//SEARCHs
	
	public List<UserDTO> searchUserByUsername(String username){
		List<User> users = userRepository.findByUsernameContaining(username);
				
		List<UserDTO> usersDTO = toListUserDTO(users);
		
		return usersDTO;
	}
	
	//REMOVEs
	

	
	
	
	
	//-----------------------------------------------------------------------------------------------
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
		
		if(user == null) return null;
		
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setGender(user.getGender());
		userDTO.setDateOfBirth(user.getDateOfBirth());
		userDTO.setPlaceOfResidence(user.getPlaceOfResidence());
		userDTO.setUsername(user.getUsername());
		
		if(user.getProfileImageURL() != null) {
			FileSystemResource image = getProfileImage(user.getProfileImageURL());
			userDTO.setProfileImage(image);
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
	
	/*
	//UTILS
	
	public Long getIdByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) return null;
		return user.getId();
	}
	
	public String getUsernameById(long id) {
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) return null;
		return user.get().getUsername();
	}
	*/
}
