package com.natour.server.application.services;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.natour.server.FileSystemUtils;
import com.natour.server.application.dtos.MessageDTO;
import com.natour.server.application.dtos.SuccessMessageDTO;
import com.natour.server.application.dtos.UserDTO;
import com.natour.server.application.dtos.request.OptionalInfoUserRequestDTO;
import com.natour.server.application.dtos.response.UserResponseDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.OptionalInfoUserDTOInvalidException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserOptionalInfoUpdateFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserProfileImageNullException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserProfileImageSaveFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserProfileImageUpdateFailureException;
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
	public MessageDTO addUser(String username) {
		if(username == null) throw new UserUsernameNullException();
		
		User user = userRepository.findByUsername(username);
		if(user != null) throw new UserUsernameUniqueException();
		
		user = new User(username);
		User result = userRepository.save(user);
		
		//return toUserDTO(result);
		return new SuccessMessageDTO();
	}
	
	
	//UPDATEs
	public MessageDTO updateProfileImage(String username,  MultipartFile image) {
		
		if(username == null) throw new UserUsernameNullException();
		
		User user = userRepository.findByUsername(username);
		if(user == null) throw new UserNotFoundException();
		
		if(image == null) throw new UserProfileImageNullException();
		
		//TODO verifica che l'immagine abbia una grandezza (in px) minima,
		//TODO abbia una grandiezza (in kb) massima
		//TODO e che sia di un formato valido
			
		/*
		//Non necessaria
		if(user.getProfileImageURL() != null) {
			//TODO rimuovi l'immagine dal fileSystem
		}
		*/
		
		String imageUrl;
		try {
			imageUrl = fileSystemRepository.save("profileImage-" + username, image.getBytes());
		}
		catch (IOException e) {
			throw new UserProfileImageSaveFailureException(e);
		}
		
		user.setProfileImageURL(imageUrl);
		
		//int updatedElement = userRepository.updateProfileImageURL(username , imageUrl);
		//if(updatedElement == 0) throw new UserProfileImageUpdateFailureException();
		
		User result = userRepository.save(user);
		
		//User result = userRepository.findByUsername(username);
		return new SuccessMessageDTO();
	}
	
	
	
	public MessageDTO updateOptionalInfo(String username, OptionalInfoUserRequestDTO optionalInfoDTO) {
		
		if(username == null) throw new UserUsernameNullException();
		
		if(!isValidDTO(optionalInfoDTO)) throw new OptionalInfoUserDTOInvalidException();
		
		User user = userRepository.findByUsername(username);
		if(user == null) throw new UserNotFoundException();
		
		
		
		String  placeOfResidence = optionalInfoDTO.getPlaceOfResidence();
		Timestamp dateOfBirth = null;
		
		String stringDateOfBirth = optionalInfoDTO.getDateOfBirth();
		if(stringDateOfBirth != null) {
			dateOfBirth =  Timestamp.valueOf(stringDateOfBirth);
		}
		 
		user.setPlaceOfResidence(placeOfResidence);
		user.setDateOfBirth(dateOfBirth);

		/*	
		int updatedElement = userRepository.updateOptionalInfo(username, placeOfResidence, timestampOfBirth, gender);
		if(updatedElement == 0) throw new UserOptionalInfoUpdateFailureException();
		*/
		
		
		
		User result = userRepository.save(user);
		return new SuccessMessageDTO();
	}
	
	
	
	//FINDs
	public UserResponseDTO findUserById(long id) {		
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) throw new UserNotFoundException();
		
		return toUserResponseDTO(user.get());
	}
	
	public UserResponseDTO findUserByUsername(String username) {
		if(username == null) throw new UserUsernameNullException();
		
		User user = userRepository.findByUsername(username);
		if(user == null) throw new UserNotFoundException();
		return toUserResponseDTO(user);
	}
	
	
	public Resource findUserImageById(long id) {
		Optional<User> optUser = userRepository.findById(id);
		if(!optUser.isPresent()) throw new UserNotFoundException();
		
		User user = optUser.get();
		
		if(user.getProfileImageURL() != null) {
			FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(user.getProfileImageURL());
			//TODO check error
			return fileSystemResource;
		}
		else return null;
	}
	
	public Resource findUserImageByUsername(String username) {		
		User user = userRepository.findByUsername(username);
		
		if(user.getProfileImageURL() != null) {
			FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(user.getProfileImageURL());
			//TODO check error
			return fileSystemResource;
		}
		else return null;
	}
	
	
	//SEARCHs
	
	public List<UserResponseDTO> searchUserByUsername(String username){
		if(username == null) throw new UserUsernameNullException();
		
		List<User> users = userRepository.findByUsernameContaining(username);
				
		List<UserResponseDTO> usersDTO = toListUserResponseDTO(users);
		
		return usersDTO;
	}
	
	//REMOVEs
	

	
	
	
	
	//-----------------------------------------------------------------------------------------------

	
	
	
	
	
	//MAPPER
	/*
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
			//userDTO.setProfileImage(profileImage);
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
	
	*/
	
	//--
	
	public UserResponseDTO toUserResponseDTO(User user) {
		
		if(user == null) return null;
		
		UserResponseDTO dto = new UserResponseDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setPlaceOfResidence(user.getPlaceOfResidence());
		
		if(user.getDateOfBirth() != null) {
			Date dateOfBirth = new Date(user.getDateOfBirth().getTime());
	        DateFormat dateFormat = new SimpleDateFormat();
			String stringDate = dateFormat.format(dateOfBirth);
			
			dto.setDateOfBirth(stringDate);
		}
		else dto.setDateOfBirth(null);
		
		return dto;
	}
	
	
	public List<UserResponseDTO> toListUserResponseDTO(List<User> users){
		if(users == null) return null;
		
		List<UserResponseDTO> dto = new LinkedList<UserResponseDTO>();
		for(User user : users) {
			dto.add(toUserResponseDTO(user));
		}
		return dto;
	}
	
	
	//VALIDATORs
	public boolean isValidDTO(OptionalInfoUserRequestDTO optionalInfoDTO) {
		if(optionalInfoDTO == null) return false;
		
		String stringDate = optionalInfoDTO.getDateOfBirth();
		if(stringDate != null) {
			DateFormat dateFormat = new SimpleDateFormat();
            Date date = null;
            try { date = (Date) dateFormat.parse(stringDate); }
            catch (ParseException e) {
                e.printStackTrace();
                //TODO EXCEPTION
                return false;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            Calendar currentCalendar = Calendar.getInstance();

            if(!calendar.before(currentCalendar)){
                //TODO EXCEPTION
                return false;
            }
		}
		
		return true;
		
	}


	
}
