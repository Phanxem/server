package com.natour.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApi;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiClientBuilder;
import com.natour.server.data.dao.implemented.MessageDAOImpl;

@Configuration
public class AwsApiGatewayWebsocketConfig {

	@Value("${amazon.aws.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.secretkey}")
    private String secretKey;
	
    @Value("${amazon.apigateway.websocket.endpoint}")
    private String endpoint;
    
    @Value("${amazon.apigateway.websocket.region}")
    private String region;


    @Bean
    public AmazonApiGatewayManagementApi amazonApiGatewayManagementApi(){
    	AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        
    	BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
    	
        AmazonApiGatewayManagementApi amazonApiGatewayManagementApi = AmazonApiGatewayManagementApiClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
                
        return amazonApiGatewayManagementApi;
    }
    
    @Bean
	MessageDAOImpl messageDAOImpl() {
		return new MessageDAOImpl();
	}
    
}
