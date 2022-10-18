package com.natour.server.data.dao.implemented;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.data.dao.interfaces.CognitoUserDAO;

public class CognitoUserDAOImpl implements CognitoUserDAO{

	@Autowired
	private AWSCognitoIdentityProvider awsCognitoIdentityProvider;
	
	@Value("${amazon.cognito.userPoolId}")
    private String userPoolId;
	
	@Override
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

}
