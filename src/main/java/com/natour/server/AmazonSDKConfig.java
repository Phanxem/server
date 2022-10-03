package com.natour.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApi;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiClientBuilder;

@Configuration
public class AmazonSDKConfig {

	/*
	
    @Bean
    public AmazonApiGatewayManagementApi settingAPIGWConnection(){
        AmazonApiGatewayManagementApiClientBuilder builder = AmazonApiGatewayManagementApiClientBuilder.standard();
        String endpointUri = "https://hren0i7ir6.execute-api.eu-west-1.amazonaws.com/production";

        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                endpointUri, "eu-west-1"
        );

        AmazonApiGatewayManagementApi agma = builder
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
        return agma;
    }
    */
}
