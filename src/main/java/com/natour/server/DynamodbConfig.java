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
import com.natour.server.data.entities.ChatConnection;

import antlr.StringUtils;

@Configuration
//@EnableDynamoDBRepositories
//(basePackages = "com.baeldung.spring.data.dynamodb.repositories")
public class DynamodbConfig {

	/*
	
    @Value("${amazon.dynamodb.endpoint}")
    private String endpoint;
    
    @Value("${amazon.dynamodb.region}")
    private String region;

    @Value("${amazon.aws.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.secretkey}")
    private String secretKey;

*/    
    /*
    
    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
    	BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
    	return new AWSStaticCredentialsProvider(credentials);
    }
    
    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
    	return DynamoDBMapperConfig.DEFAULT;
    }
    
    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
    	return new DynamoDBMapper(amazonDynamoDB, config);
    }
    
    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
    	AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    	
    	AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
    	.withCredentials(amazonAWSCredentialsProvider())
    	.withEndpointConfiguration(endpointConfiguration)
    	.build();
    	
    	return amazonDynamoDB;
    }
    
    */
    
/*
    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(amazonAWSCredentials());
        
        if (endpoint.isEmpty()) amazonDynamoDB.setEndpoint(endpoint);
        
        return amazonDynamoDB;
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }
    */
    
    
    
    
   /* 
    private AmazonDynamoDB buildAmazonDynamoDB() {
    	AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    	BasicAWSCredentials basicCredentials = new BasicAWSCredentials(accessKey, secretKey);
    	AWSStaticCredentialsProvider credentials = new AWSStaticCredentialsProvider(basicCredentials);
    	
    	
    	AmazonDynamoDB result = AmazonDynamoDBClientBuilder
    			.standard()
    			.withEndpointConfiguration(endpointConfiguration)
    			.withCredentials(credentials)
    			.build();
    	
    	return result;
    }
    */
    
    
    /*
    
    @Bean
    public DynamoDBMapper dynamoDBMapper() {
    	
    	AmazonDynamoDB amazonDynamoDB = buildAmazonDynamoDB();
    	DynamoDBMapper dynamoDBMapper =  new DynamoDBMapper(amazonDynamoDB);
    	
    	init(dynamoDBMapper, amazonDynamoDB);
    	
    	return dynamoDBMapper;
    }
    
    
    
    @Bean
    public AmazonDynamoDB buildAmazonDynamoDB() {
    	//BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    	//AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(credentials);
        //if (endpoint.isEmpty()) amazonDynamoDB.setEndpoint(endpoint);
        
    	AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
    			.standard()
    			.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
    			.build();
    	
        return amazonDynamoDB;
    }
    
    

    
    public void init(DynamoDBMapper dynamoDBMapper, AmazonDynamoDB amazonDynamoDB) {
    	CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(ChatConnection.class);
        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        if (TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest)) {
            System.out.println("Table created");
        }
    }
    
    */
    
}