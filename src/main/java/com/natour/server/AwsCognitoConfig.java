package com.natour.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.natour.server.data.dao.implemented.CognitoUserDAOImpl;
import com.natour.server.data.dao.implemented.ImageDAOImpl;

@Configuration
public class AwsCognitoConfig {

    @Value("${amazon.aws.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.secretkey}")
    private String secretKey;
	
    @Value("${amazon.cognito.clientId}")
    private String clientId;
    
    @Value("${amazon.cognito.userPoolId}")
    private String userPoolId;
    
    @Value("${amazon.cognito.region}")
    private String region;

	
	
	 @Bean
	 public AWSCognitoIdentityProvider awsCognitoIdentityProvider() {

		 BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
	    	
		 AWSCognitoIdentityProvider awsCognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
				 //.withCredentials(amazonAWSCredentialsProvider())
	    	//.withCredentials(amazonAWSCredentials())
				 .withCredentials(new AWSStaticCredentialsProvider(credentials))
				 .withRegion(region)
				 .build();
	    	
		 return awsCognitoIdentityProvider;
	 }
	 
		@Bean
		public CognitoUserDAOImpl cognitoUserDAOImpl() {
			return new CognitoUserDAOImpl();
		}

}
