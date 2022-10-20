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
import com.amazonaws.services.cognitoidp.model.AdminLinkProviderForUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminLinkProviderForUserResult;
import com.amazonaws.services.cognitoidp.model.ProviderUserIdentifierType;
import com.natour.server.application.dtos.request.SaveUserRequestDTO;
import com.natour.server.application.dtos.request.SaveUserOptionalInfoRequestDTO;
import com.natour.server.application.dtos.response.GetResourceResponseDTO;
import com.natour.server.application.dtos.response.GetListUserResponseDTO;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.dtos.response.StringResponseDTO;
import com.natour.server.application.dtos.response.GetUserResponseDTO;
import com.natour.server.application.services.utils.DateUtils;
import com.natour.server.application.services.utils.ResultMessageUtils;
import com.natour.server.data.dao.interfaces.CognitoUserDAO;
import com.natour.server.data.dao.interfaces.ImageDAO;
import com.natour.server.data.entities.dynamoDB.ChatConnection;
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
	//@Autowired
	//private FileSystemRepository fileSystemRepository;
	
	@Autowired
	private ChatConnectionRepository chatConnectionRepository;

	@Autowired
	private CognitoUserDAO cognitoUserDAO;

	@Autowired
	private ImageDAO imageDAO;
	
	
	private final static String IDENTITY_PROVIDER_COGNITO = "Cognito";
	private final static String IDENTITY_PROVIDER_FACEBOOK = "Facebook";
	private final static String IDENTITY_PROVIDER_GOOGLE = "Google";
	
	private final static String COGNITO_SUBJECT ="Cognito_Subject";
	
	private final static int IMAGE_MIN_HEIGHT_PX = 300;
	private final static int IMAGE_MIN_WIDTH_PX = 300;
	
	private final static int IMAGE_MAX_SIZE_BYTES = 1000000;
	
	private final static int USER_PER_PAGE = 20;
	
	
	//ADDs
	public ResultMessageDTO addUser(SaveUserRequestDTO addUserRequestDTO) {	
		if(!isValidDTO(addUserRequestDTO)) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		User user = userRepository.findByIdentityProviderAndIdIdentityProvided(addUserRequestDTO.getIdentityProvider(), addUserRequestDTO.getIdIdentityProvided());
		if(user != null) {
			return ResultMessageUtils.ERROR_MESSAGE_UNIQUE_VIOLATION;
		}
		
		user = new User(addUserRequestDTO.getIdentityProvider(), addUserRequestDTO.getIdIdentityProvided(), addUserRequestDTO.getUsername());
		User result = userRepository.save(user);
		
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
	
	
	
	//UPDATEs
	public ResultMessageDTO updateProfileImage(long idUser,  MultipartFile image) {
		
		if(idUser < 0) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(optionalUser.isEmpty()) {
			return ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND;
		}
		User user = optionalUser.get();
		
		if(!isValidImage(image)) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		byte[] imageBytes = null;
		try {
			imageBytes = image.getBytes();
		}
		catch (IOException e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		StringResponseDTO stringResponseDTO = imageDAO.put(String.valueOf(user.getId()), imageBytes);
		ResultMessageDTO resultMessageDTO = stringResponseDTO.getResultMessage();
		if(!ResultMessageUtils.isSuccess(resultMessageDTO)) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		String imageUrl = stringResponseDTO.getString();
		
		/*
		String imageUrl;
		try {
			imageUrl = fileSystemRepository.save("profileImage-" + user.getId(), image.getBytes());
		}
		catch (IOException e) {
			//TODO
			throw new UserProfileImageSaveFailureException(e);
		}
		*/
		
		
		if(user.getProfileImageKey() != null) {
			imageDAO.delete(user.getProfileImageKey());
		}
		
		user.setProfileImageKey(imageUrl);
		
		User result = userRepository.save(user);
		
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
	
	
	
	public ResultMessageDTO updateOptionalInfo(long idUser, SaveUserOptionalInfoRequestDTO optionalInfoUserRequestDTO) {
		
		if(idUser < 0) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		if(!isValidDTO(optionalInfoUserRequestDTO)) {
			return ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST;
		}
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(optionalUser.isEmpty()) {
			return ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND;
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
				return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
			}
			dateOfBirth = new Timestamp(calendar.getTimeInMillis());
		}
		 
		user.setPlaceOfResidence(placeOfResidence);
		user.setDateOfBirth(dateOfBirth);
		
		User result = userRepository.save(user);
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
	
	
	//FINDs
	public GetUserResponseDTO findUserById(long idUser) {		
		GetUserResponseDTO userResponseDTO = new GetUserResponseDTO();
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(!optionalUser.isPresent()) {
			userResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_FAILURE);
			return userResponseDTO;
		}
		User user = optionalUser.get();
		
		userResponseDTO =  toUserResponseDTO(user);
		
		return userResponseDTO;
	}
	

	public GetUserResponseDTO findUserByIdp(String identityProvider, String idIdentityProvided) {
		GetUserResponseDTO userResponseDTO = new GetUserResponseDTO();
		
		User user = userRepository.findByIdentityProviderAndIdIdentityProvided(identityProvider, idIdentityProvided);
		if(user == null) {
			userResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return userResponseDTO;
		}
		
		userResponseDTO =  toUserResponseDTO(user);
		
		return userResponseDTO;
	}
	
	public GetResourceResponseDTO findUserImageById(long idUser) {
		GetResourceResponseDTO imageResponseDTO = new GetResourceResponseDTO();
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(!optionalUser.isPresent()) {
			imageResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return imageResponseDTO;
		}
		User user = optionalUser.get();
		

		if(user.getProfileImageKey() == null) {
			imageResponseDTO.setResource(null);
			imageResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
			return imageResponseDTO;
		}
		
		imageResponseDTO = imageDAO.getByName(user.getProfileImageKey());
			
		/*
		FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(user.getProfileImageURL());
		if(fileSystemResource != null) {
			imageResponseDTO.setResource(fileSystemResource);
			imageResponseDTO.setResultMessage(new ResultMessageDTO());
				
			return imageResponseDTO;
		}
			
		imageResponseDTO.setResultMessage(new ResultMessageDTO(-100, "errore1"));
		*/
		
		return imageResponseDTO;
	}

	
	public GetUserResponseDTO findUserByIdConnection(String idConnection) {
		GetUserResponseDTO userResponseDTO = new GetUserResponseDTO();
		
		ChatConnection chatConnection = chatConnectionRepository.findById(idConnection);
		
		long idUser = Long.valueOf(chatConnection.getIdUser());
		
		if(idUser < 0) {
			userResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST);
			return userResponseDTO;
		}
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(!optionalUser.isPresent()) {
			userResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return userResponseDTO;
		}
		
		User user = optionalUser.get();
		
		userResponseDTO = toUserResponseDTO(user);
		
		return userResponseDTO;
	}
	
	//GETs
	
	public GetListUserResponseDTO searchUserByUsername(String username, int page){
		GetListUserResponseDTO listUserResponseDTO = new GetListUserResponseDTO();
		
		if(username == null) {
			listUserResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST);
			return listUserResponseDTO;
		}
		Pageable pageable = PageRequest.of(page, USER_PER_PAGE);
		List<User> users = userRepository.findByUsernameContaining(username, pageable);
		
		listUserResponseDTO = toListUserResponseDTO(users);
		
		return listUserResponseDTO;
	}
	
	public GetListUserResponseDTO searchUserWithConversation(long idUser, int page){
		GetListUserResponseDTO listUserResponseDTO = new GetListUserResponseDTO();
		
		if(idUser < 0) {
			listUserResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_INVALID_REQUEST);
			return listUserResponseDTO;
		}
		
		Optional<User> optionalUser = userRepository.findById(idUser);
		if(!optionalUser.isPresent()) {
			listUserResponseDTO.setResultMessage(ResultMessageUtils.ERROR_MESSAGE_NOT_FOUND);
			return listUserResponseDTO;
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
	    
	    listUserResponseDTO = toListUserResponseDTO(pagedUsers);
		
		return listUserResponseDTO;
	}
	
	
	//REMOVEs
	public ResultMessageDTO deleteCognitoUser(String idIdentityProvided) {
		
		ResultMessageDTO resultMessageDTO = cognitoUserDAO.deleteCognitoUser(idIdentityProvided);
		
		return resultMessageDTO;
	}

	
	
	
	
	//-----------------------------------------------------------------------------------------------

	public static GetUserResponseDTO toUserResponseDTO(User user) {
		
		if(user == null) return null;
		
		GetUserResponseDTO dto = new GetUserResponseDTO();
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
		
		dto.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
		
		return dto;
	}
	
	public static GetListUserResponseDTO toListUserResponseDTO(List<User> users){
		if(users == null) return null;
		
		List<GetUserResponseDTO> dto = new LinkedList<GetUserResponseDTO>();
		for(User user : users) {
			dto.add(toUserResponseDTO(user));
		}
		
		GetListUserResponseDTO listUserResponseDTO = new GetListUserResponseDTO();
		listUserResponseDTO.setListUser(dto);
		listUserResponseDTO.setResultMessage(ResultMessageUtils.SUCCESS_MESSAGE);
		
		return listUserResponseDTO;
	}
	
	
	//VALIDATORs
	public static boolean isValidDTO(SaveUserOptionalInfoRequestDTO optionalInfoDTO) {
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

	public static boolean isValidDTO(SaveUserRequestDTO dto) {
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
