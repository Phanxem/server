package com.natour.server.application.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.natour.server.DateUtils;
import com.natour.server.FileSystemUtils;
import com.natour.server.application.dtos.request.AddUserRequestDTO;
import com.natour.server.application.dtos.request.UpdateUserOptionalInfoRequestDTO;
import com.natour.server.application.dtos.response.ImageResponseDTO;
import com.natour.server.application.dtos.response.ListUserResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.UserResponseDTO;
import com.natour.server.application.exceptionHandler.serverExceptions.OptionalInfoUserDTOInvalidException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserNotFoundException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserOptionalInfoUpdateFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserProfileImageNullException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserProfileImageSaveFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserProfileImageUpdateFailureException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserUsernameNullException;
import com.natour.server.application.exceptionHandler.serverExceptions.UserUsernameUniqueException;
import com.natour.server.data.entities.Chat;
import com.natour.server.data.entities.Message;
import com.natour.server.data.entities.User;
import com.natour.server.data.repository.ChatRepository;
import com.natour.server.data.repository.FileSystemRepository;
import com.natour.server.data.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FileSystemRepository fileSystemRepository;
	
	@Autowired
	private ChatRepository chatRepository;

	
	private final static String IDENTITY_PROVIDER_COGNITO = "Cognito";
	private final static String IDENTITY_PROVIDER_FACEBOOK = "Facebook";
	private final static String IDENTITY_PROVIDER_GOOGLE = "Google";
	
	private final static int IMAGE_MIN_HEIGHT_PX = 300;
	private final static int IMAGE_MIN_WIDTH_PX = 300;
	
	private final static int IMAGE_MAX_SIZE_BYTES = 1000000;
	
	private final static int USER_PER_PAGE = 20;
	
	
	//ADDs
	public ResultMessageDTO addUser(AddUserRequestDTO addUserRequestDTO) {
		if(!isValidDTO(addUserRequestDTO)) {
			//TODO new exception
			throw new UserUsernameNullException();
		}
		
		User user = userRepository.findByIdentityProviderAndIdIdentityProvided(addUserRequestDTO.getIdentityProvider(), addUserRequestDTO.getIdIdentityProvided());
		if(user != null) {
			//TODO new exception
			throw new UserUsernameUniqueException();
		}
		
		user = new User(addUserRequestDTO.getIdentityProvider(), addUserRequestDTO.getIdIdentityProvided(), addUserRequestDTO.getUsername());
		User result = userRepository.save(user);
		
		return new ResultMessageDTO();
	}
	
	
	
	//UPDATEs
	public ResultMessageDTO updateProfileImage(long idUser,  MultipartFile image) {
		
		if(idUser < 0) {
			//TODO new exception
			throw new UserUsernameNullException();
		}
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(optionalUser.isEmpty()) {
			//TODO
			throw new UserNotFoundException();
		}
		User user = optionalUser.get();
		
		
		if(!isValidImage(image)) {
			//TODO
			throw new UserProfileImageNullException();
		}
		
		String imageUrl;
		try {
			imageUrl = fileSystemRepository.save("profileImage-" + user.getId(), image.getBytes());
		}
		catch (IOException e) {
			//TODO
			throw new UserProfileImageSaveFailureException(e);
		}
		
		if(user.getProfileImageURL() != null) {
			//TODO rimuovi l'immagine dal fileSystem
		}
		
		user.setProfileImageURL(imageUrl);
		
		User result = userRepository.save(user);
		
		return new ResultMessageDTO();
	}
	
	
	
	public ResultMessageDTO updateOptionalInfo(long idUser, UpdateUserOptionalInfoRequestDTO optionalInfoUserRequestDTO) {
		
		if(idUser < 0) {
			//TODO
			throw new UserUsernameNullException();
		}
		
		if(!isValidDTO(optionalInfoUserRequestDTO)) {
			//TODO
			throw new OptionalInfoUserDTOInvalidException();
		}
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(optionalUser.isEmpty()) {
			//TODO
			throw new UserNotFoundException();
		}
		User user = optionalUser.get();
		
		
		String  placeOfResidence = optionalInfoUserRequestDTO.getPlaceOfResidence();
		
		Timestamp dateOfBirth = null;
		String stringDateOfBirth = optionalInfoUserRequestDTO.getDateOfBirth();
		if(stringDateOfBirth != null) {
			
			Calendar calendar = null;
			try {
				calendar = DateUtils.toCalendar(stringDateOfBirth);
			}
			catch (ParseException e) {
				// TODO
				e.printStackTrace();
			}
			dateOfBirth = new Timestamp(calendar.getTimeInMillis());
		}
		 
		user.setPlaceOfResidence(placeOfResidence);
		user.setDateOfBirth(dateOfBirth);
		
		User result = userRepository.save(user);
		return new ResultMessageDTO();
	}
	
	
	
	//FINDs
	public UserResponseDTO findUserById(long idUser) {		
		Optional<User> user = userRepository.findById(idUser);
		if(!user.isPresent()) {
			//TODO
			throw new UserNotFoundException();
		}
		
		return toUserResponseDTO(user.get());
	}
	

	public UserResponseDTO findUserByIdp(String identityProvider, String idIdentityProvided) {
		User user = userRepository.findByIdentityProviderAndIdIdentityProvided(identityProvider, idIdentityProvided);
		if(user == null) {
			//TODO
			throw new UserNotFoundException();
		}
		
		return toUserResponseDTO(user);
	}
	
	public ImageResponseDTO findUserImageById(long idUser) {
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(!optionalUser.isPresent()) {
			//TODO
			throw new UserNotFoundException();
		}
		User user = optionalUser.get();
		
		ImageResponseDTO imageResponseDTO = new ImageResponseDTO();
		if(user.getProfileImageURL() != null) {
			FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(user.getProfileImageURL());
			if(fileSystemResource != null) {
				imageResponseDTO.setImage(fileSystemResource);
				imageResponseDTO.setResultMessage(new ResultMessageDTO());
				
				return imageResponseDTO;
			}
			imageResponseDTO.setResultMessage(new ResultMessageDTO(-100, "errore1"));
			return imageResponseDTO;
		}
		imageResponseDTO.setResultMessage(new ResultMessageDTO(-100, "errore1"));
		return imageResponseDTO;
	}

	
	
	
	//SEARCHs
	
	public ListUserResponseDTO searchUserByUsername(String username, int page){
		if(username == null) {
			//TODO
			throw new UserUsernameNullException();
		}
		Pageable pageable = PageRequest.of(page, USER_PER_PAGE);
		List<User> users = userRepository.findByUsernameContaining(username, pageable);
		
		ListUserResponseDTO listUserResponseDTO = toListUserResponseDTO(users);
		
		return listUserResponseDTO;
	}
	
	
	
	public ListUserResponseDTO searchUserWithConversation(long idUser, int page){
		if(idUser < 0) {
			//TODO
			throw new UserUsernameNullException();
		}
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(!optionalUser.isPresent()) {
			//TODO
			throw new UserNotFoundException();
		}
		User user = optionalUser.get();
		
		List<Chat> chats = user.getChats();
		Map<User,Timestamp> mapUsers = new LinkedHashMap<User,Timestamp>();
		
		for(Chat chat: chats) {
			List<Message> messages = chat.getMessages();
			Collections.sort(messages);
			Collections.reverse(messages);
			
			Message lastMessage = messages.get(0);
			
			List<User> users = chat.getUsers();
			User otherUser = users.get(0);
			if(otherUser.getId() == user.getId()) otherUser = users.get(1);
			
			mapUsers.put(otherUser, lastMessage.getDateOfInput());
		}
		
		//---
		
		List<Map.Entry<User, Timestamp>> entries = new ArrayList<>(mapUsers.entrySet());

	    Collections.sort(entries, new Comparator<Map.Entry<User, Timestamp>>() {
	        @Override
	        public int compare(Map.Entry<User, Timestamp> map1, Map.Entry<User, Timestamp> map2) {
	            Timestamp timestamp1 = map1.getValue();
	            Timestamp timestamp2 = map2.getValue();
	        	
	        	return -(timestamp1.compareTo(timestamp2));
	        }
	    });

	    List<User> users = new LinkedList<User>();
	    for(Map.Entry<User, Timestamp> entry : entries) {
	        User tempUser = entry.getKey();
	        users.add(tempUser);
	    }
		
		List<User> pagedUsers = users.subList(page * USER_PER_PAGE, (page + 1) * USER_PER_PAGE);
		
		
		ListUserResponseDTO listUserResponseDTO = toListUserResponseDTO(pagedUsers);
		
		return listUserResponseDTO;
	}
	
	
	public ListUserResponseDTO findUsersByIdChat(long idChat) {
		Optional<Chat> optionalChat = chatRepository.findById(idChat);
		if(optionalChat.isEmpty()) {
			//TODO
			return null;
		}
		//Chat chat = optionalChat.get();
		

		List<User> users = userRepository.findByChat_id(idChat);
		
		return UserService.toListUserResponseDTO(users);
	}
	
	
	//REMOVEs
	public ResultMessageDTO deleteCognitoUser(String idIdentityProvided) {
		
		//get user from cognito WITH AdminGetUser
		//check if it's unconfirmed, WITH UserStatus
		//if unconfirmed, delete user WITH AdminDeleteUser
		
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	//-----------------------------------------------------------------------------------------------

	public static UserResponseDTO toUserResponseDTO(User user) {
		
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
		
		dto.setResultMessage(new ResultMessageDTO());
		
		return dto;
	}
	
	public static ListUserResponseDTO toListUserResponseDTO(List<User> users){
		if(users == null) return null;
		
		List<UserResponseDTO> dto = new LinkedList<UserResponseDTO>();
		for(User user : users) {
			dto.add(toUserResponseDTO(user));
		}
		
		ListUserResponseDTO listUserResponseDTO = new ListUserResponseDTO();
		listUserResponseDTO.setListUser(dto);
		listUserResponseDTO.setResultMessage(new ResultMessageDTO());
		
		return listUserResponseDTO;
	}
	
	
	//VALIDATORs
	public static boolean isValidDTO(UpdateUserOptionalInfoRequestDTO optionalInfoDTO) {
		if(optionalInfoDTO == null) return false;
		
		String stringDate = optionalInfoDTO.getDateOfBirth();
		if(stringDate != null) {
			
			Calendar calendar = null;
			try {
				calendar = DateUtils.toCalendar(stringDate);
			}
			catch (ParseException e) {
				e.printStackTrace();
				return false;
			}
		
			//VERIFICA CHE LA STRINGA NON SIA PRECEDENTE A QUELLA ATTUALE
            Calendar currentCalendar = Calendar.getInstance();
            if(!calendar.before(currentCalendar)){
                return false;
            }
		}
		
		return true;
	}

	public static boolean isValidDTO(AddUserRequestDTO dto) {
		String username = dto.getUsername();
		if(username == null || username.isBlank()) return false;
		
		String identityProvider = dto.getIdentityProvider();
		if(identityProvider == null || 
		   identityProvider.isBlank() ||
		   !identityProvider.equals(IDENTITY_PROVIDER_COGNITO) ||
		   !identityProvider.equals(IDENTITY_PROVIDER_FACEBOOK) ||
		   !identityProvider.equals(IDENTITY_PROVIDER_GOOGLE))
		{
			return false;
		}
		
		String idIdentityProvided = dto.getIdIdentityProvided();
		if(idIdentityProvided == null || idIdentityProvided.isBlank()) return false;
		
		return true;
	}

	public static boolean isValidImage(MultipartFile image) {
		
		if(image == null || image.isEmpty()) return false;
		
		InputStream inputStream = null;
		try {
			inputStream = image.getInputStream();
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(inputStream);
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		int width = bufferedImage.getWidth();
		if(width < IMAGE_MIN_WIDTH_PX) return false;
		
		int height = bufferedImage.getHeight();
		if(height < IMAGE_MIN_HEIGHT_PX) return false;
		
		
		long imageSizeBytes = image.getSize();
		
		if(imageSizeBytes > IMAGE_MAX_SIZE_BYTES) return false;
		
		return true;
	}




/*	
	//utils
	static <K, V> void orderByValue(LinkedHashMap<K, V> m, final Comparator<? super V> c) {
	    List<Map.Entry<K, V>> entries = new ArrayList<>(m.entrySet());

	    Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
	        @Override
	        public int compare(Map.Entry<K, V> lhs, Map.Entry<K, V> rhs) {
	            return c.compare(lhs.getValue(), rhs.getValue());
	        }
	    });

	    m.clear();
	    for(Map.Entry<K, V> e : entries) {
	        m.put(e.getKey(), e.getValue());
	    }
	}
*/
	
	
	static List<User> orderByValue(Map<User, Timestamp> map) {
	    List<Map.Entry<User, Timestamp>> entries = new ArrayList<>(map.entrySet());

	    Collections.sort(entries, new Comparator<Map.Entry<User, Timestamp>>() {
	        @Override
	        public int compare(Map.Entry<User, Timestamp> map1, Map.Entry<User, Timestamp> map2) {
	            Timestamp timestamp1 = map1.getValue();
	            Timestamp timestamp2 = map2.getValue();
	        	
	        	return -(timestamp1.compareTo(timestamp2));
	        }
	    });

	    List<User> users = new LinkedList<User>();
	    for(Map.Entry<User, Timestamp> entry : entries) {
	        User user = entry.getKey();
	        users.add(user);
	    }
	    
	    return users;
	}

	
}
