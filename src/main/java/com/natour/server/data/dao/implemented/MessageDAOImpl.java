package com.natour.server.data.dao.implemented;

import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApi;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiClientBuilder;
import com.amazonaws.services.apigatewaymanagementapi.model.GetConnectionRequest;
import com.amazonaws.services.apigatewaymanagementapi.model.GetConnectionResult;
import com.amazonaws.services.apigatewaymanagementapi.model.PostToConnectionRequest;
import com.amazonaws.services.apigatewaymanagementapi.model.PostToConnectionResult;
import com.natour.server.application.dtos.response.ResultMessageDTO;
import com.natour.server.application.services.utils.ResultMessageUtils;
import com.natour.server.data.dao.interfaces.MessageDAO;

@Component
public class MessageDAOImpl implements MessageDAO{
	
	@Autowired
	private AmazonApiGatewayManagementApi amazonApiGatewayManagementApi;

	@Override
	public ResultMessageDTO sendMessage(String idConnection, String message) {
		
		String stringMessage = "{\"message\":\"" + message + "\" }";
		ByteBuffer byteBufferMessage = ByteBuffer.wrap(stringMessage.getBytes());
					
		System.out.println("idConnection: " + idConnection + " | message: " + message );
				
		PostToConnectionRequest postToConnectionRequest = new PostToConnectionRequest();
		postToConnectionRequest.setConnectionId(idConnection);
		postToConnectionRequest.setData(byteBufferMessage);

					
		PostToConnectionResult postToConnectionResult = null;

		try {
			postToConnectionResult = amazonApiGatewayManagementApi.postToConnection(postToConnectionRequest);
		}
		catch(Exception e) {
			return ResultMessageUtils.ERROR_MESSAGE_FAILURE;
		}
		
		return ResultMessageUtils.SUCCESS_MESSAGE;
	}
}