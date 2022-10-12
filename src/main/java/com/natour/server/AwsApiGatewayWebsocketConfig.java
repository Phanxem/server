package com.natour.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApi;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiClientBuilder;

@Configuration
public class AwsApiGatewayWebsocketConfig {

	
	/*
	
    @Value("${amazon.dynamodb.endpoint}")
    private String endpoint;
    
    @Value("${amazon.dynamodb.region}")
    private String region;

*/ 
	
	/*
	
    @Bean
    public AmazonApiGatewayManagementApi amazonApiGatewayWebsocket(){
    	AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        
        AmazonApiGatewayManagementApi amazonApiGatewayManagementApi = AmazonApiGatewayManagementApiClientBuilder.standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
                
        return amazonApiGatewayManagementApi;
    }
    */
}
