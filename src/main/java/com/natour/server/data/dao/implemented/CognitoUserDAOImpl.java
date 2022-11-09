package com.natour.server.data.dao.implemented;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.services.utils.ResultMessageUtils;
import com.natour.server.data.dao.interfaces.CognitoUserDAO;

public class CognitoUserDAOImpl implements CognitoUserDAO{

	@Autowired
	private AWSCognitoIdentityProvider awsCognitoIdentityProvider;
	
	@Value("${amazon.cognito.userPoolId}")
    private String userPoolId;
	
	
	@Override
	public ResultMessageDTO deleteCognitoUser(String idIdentityProvided) {
		AdminGetUserRequest adminGetUserRequest = new AdminGetUserRequest();
		adminGetUserRequest.setUsername(idIdentityProvided);
		adminGetUserRequest.setUserPoolId(userPoolId);
		
		AdminGetUserResult adminGetUserResult = null;
		try {
			adminGetUserResult = awsCognitoIdentityProvider.adminGetUser(adminGetUserRequest);
		}
		catch(Exception e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		
		String unconfirmed = "UNCONFIRMED";
		//String unconfirmed = "FORCE_CHANGE_PASSWORD";
		
		String userStatus = adminGetUserResult.getUserStatus();
		if(!userStatus.equals(unconfirmed)) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		
		AdminDeleteUserRequest adminDeleteUserRequest = new AdminDeleteUserRequest();
		adminDeleteUserRequest.setUsername(idIdentityProvided);
		adminDeleteUserRequest.setUserPoolId(userPoolId);
		
		AdminDeleteUserResult adminDeleteUserResult = null;
		try {
			adminDeleteUserResult = awsCognitoIdentityProvider.adminDeleteUser(adminDeleteUserRequest);
		}
		catch(Exception e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}

}
