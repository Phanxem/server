package com.natour.server;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.natour.server.data.entities.dynamoDB.ChatConnection;

import antlr.StringUtils;

@Configuration
@EnableDynamoDBRepositories
(basePackages = "com.natour.server.data.repository.dynamoDB") 
public class AwsDynamoDBConfig {


    
    
    @Value("${amazon.dynamodb.region}")
    private String region;

    @Value("${amazon.aws.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.secretkey}")
    private String secretKey;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
    	
    	String endpoint = "dynamodb." + region +".amazonaws.com";
    	
    	AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    	
    	BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
    	
    	AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
    	//.withCredentials(amazonAWSCredentialsProvider())
    	//.withCredentials(amazonAWSCredentials())
    	.withCredentials(new AWSStaticCredentialsProvider(credentials))
    	.withEndpointConfiguration(endpointConfiguration)
    	.build();
    	
    	return amazonDynamoDB;
    }
    
    @Bean
    public DynamoDBMapper dynamoDBMapper() {
    	DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB(), DynamoDBMapperConfig.DEFAULT);
    	return dynamoDBMapper;
    }
    
    /*
    
    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
    	BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
    	return new AWSStaticCredentialsProvider(credentials);
    }
    
    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }
    
    
    
    //----------------
    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
    	return DynamoDBMapperConfig.DEFAULT;
    }
    
    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
    	return new DynamoDBMapper(amazonDynamoDB, config);
    }
    //-------------------
    
    
    
    
    */
    

    
}