package com.natour.server.data.dao.interfaces;

import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.natour.server.application.dtos.response.ResultMessageDTO;

public interface CognitoUserDAO {

	public ResultMessageDTO deleteCognitoUser(String idIdentityProvided);
}
