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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.natour.server.application.dtos.request.AddUserRequestDTO;
import com.natour.server.application.dtos.request.UpdateUserOptionalInfoRequestDTO;
import com.natour.server.application.dtos.response.ResourceResponseDTO;
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
import com.natour.server.application.services.utils.DateUtils;
import com.natour.server.data.entities.rds.Chat;
import com.natour.server.data.entities.rds.Message;
import com.natour.server.data.entities.rds.User;
import com.natour.server.data.repository.dynamoDB.ChatConnectionRepository;
import com.natour.server.data.repository.rds.UserRepository;
import com.natour.server.data.repository.s3.FileSystemRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FileSystemRepository fileSystemRepository;
	
	@Autowired
	private ChatConnectionRepository chatRepository;

	//DA TESTARE
	@Autowired
	private AWSCognitoIdentityProvider awsCognitoIdentityProvider;
	
	@Value("${amazon.cognito.userPoolId}")
    private String userPoolId;
	
	private final static String IDENTITY_PROVIDER_COGNITO = "Cognito";
	private final static String IDENTITY_PROVIDER_FACEBOOK = "Facebook";
	private final static String IDENTITY_PROVIDER_GOOGLE = "Google";
	
	private final static String COGNITO_SUBJECT ="Cognito_Subject";
	
	private final static int IMAGE_MIN_HEIGHT_PX = 300;
	private final static int IMAGE_MIN_WIDTH_PX = 300;
	
	private final static int IMAGE_MAX_SIZE_BYTES = 1000000;
	
	private final static int USER_PER_PAGE = 20;
	
	
	//ADDs
	public ResultMessageDTO addUser(AddUserRequestDTO addUserRequestDTO) {
		if(!isValidDTO(addUserRequestDTO)) {
			
			System.out.println("dto not valid");
			//TODO new exception
			throw new UserUsernameNullException();
		}
		
		User user = userRepository.findByIdentityProviderAndIdIdentityProvided(addUserRequestDTO.getIdentityProvider(), addUserRequestDTO.getIdIdentityProvided());
		if(user != null) {
			System.out.println("user unique constraint violation");
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
	
	/*
	public ResultMessageDTO linkToFacebook(long idUser, String idFacebook) {
		if(idUser < 0) {
			//TODO
			throw new UserUsernameNullException();
		}	
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(optionalUser.isEmpty()) {
			//TODO
			throw new UserNotFoundException();
		}
		User user = optionalUser.get();
		
		if (user.getIdentityProvider() != IDENTITY_PROVIDER_COGNITO) {
			//TODO
			System.out.println("Errore no cognito");
			return null;
		}

		AdminLinkProviderForUserRequest adminLinkProviderForUserRequest = new AdminLinkProviderForUserRequest();
		
		ProviderUserIdentifierType destinationUser = new ProviderUserIdentifierType();
		ProviderUserIdentifierType sourceUser = new ProviderUserIdentifierType();
		
		destinationUser.setProviderAttributeValue(user.getIdIdentityProvided());
		destinationUser.setProviderName(IDENTITY_PROVIDER_COGNITO);
		
		sourceUser.setProviderAttributeName(COGNITO_SUBJECT);
		sourceUser.setProviderName(IDENTITY_PROVIDER_FACEBOOK);
		sourceUser.setProviderAttributeValue(idFacebook);
		
		
		//se si effettua il link
		return null;
	}
	*/
	
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
	
	public ResourceResponseDTO findUserImageById(long idUser) {
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(!optionalUser.isPresent()) {
			//TODO
			throw new UserNotFoundException();
		}
		User user = optionalUser.get();
		
		ResourceResponseDTO imageResponseDTO = new ResourceResponseDTO();
		if(user.getProfileImageURL() != null) {
			FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(user.getProfileImageURL());
			if(fileSystemResource != null) {
				imageResponseDTO.setResource(fileSystemResource);
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
		
	    int numElements = users.size();
	    int spacesAvailable = (page+1) * USER_PER_PAGE;
	    
	    
	    List<User> pagedUsers = null;
	    //TUTTI GLI ELEMENTI NELLA PAGINA
	    if(numElements >= spacesAvailable) {
	    	pagedUsers = users.subList(page * USER_PER_PAGE, (page + 1) * USER_PER_PAGE);
	    }
	    //ALCUNI ELEMENTI NELLA PAGINA
	    else if(numElements > spacesAvailable - USER_PER_PAGE) {
	    	pagedUsers = users.subList(page * USER_PER_PAGE, numElements);
	    }
	    //NESSUN ELEMENTO NELLA PAGINA
	    else {
	    	pagedUsers = new ArrayList<User>();
	    }
	    
		ListUserResponseDTO listUserResponseDTO = toListUserResponseDTO(pagedUsers);
		
		return listUserResponseDTO;
	}
	
	

	
	//REMOVEs
	public ResultMessageDTO deleteCognitoUser(String idIdentityProvided) {
		
		ResultMessageDTO resultMessageDTO = new ResultMessageDTO();
		
		AdminGetUserRequest adminGetUserRequest = new AdminGetUserRequest();
		adminGetUserRequest.setUsername(idIdentityProvided);
		adminGetUserRequest.setUserPoolId(userPoolId);
		
		AdminGetUserResult adminGetUserResult = null;
		try {
			adminGetUserResult = awsCognitoIdentityProvider.adminGetUser(adminGetUserRequest);
		}
		catch(Exception e) {
			System.out.println("error get user FROM COGNITO");
			//TODO
			return resultMessageDTO;
		}
		
		
		
		System.out.println("get userStatus");
		//String unconfirmed = "UNCONFIRMED";
		String unconfirmed = "FORCE_CHANGE_PASSWORD";
		
		String userStatus = adminGetUserResult.getUserStatus();
		if(!userStatus.equals(unconfirmed)) {
			System.out.println("error not unconfirmed");
			//TODO
			return resultMessageDTO;
		}
		
		
		AdminDeleteUserRequest adminDeleteUserRequest = new AdminDeleteUserRequest();
		adminDeleteUserRequest.setUsername(idIdentityProvided);
		adminDeleteUserRequest.setUserPoolId(userPoolId);
		
		AdminDeleteUserResult adminDeleteUserResult = null;
		try {
			adminDeleteUserResult = awsCognitoIdentityProvider.adminDeleteUser(adminDeleteUserRequest);
		}
		catch(Exception e) {
			System.out.println("error delete user FROM COGNITO");
			//TODO
			return resultMessageDTO;
		}
		
		return resultMessageDTO;
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
		   (!identityProvider.equals(IDENTITY_PROVIDER_COGNITO) &&
			!identityProvider.equals(IDENTITY_PROVIDER_FACEBOOK) &&
			!identityProvider.equals(IDENTITY_PROVIDER_GOOGLE)
		   ))
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
