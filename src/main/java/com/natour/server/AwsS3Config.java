package com.natour.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.natour.server.data.dao.implemented.GpxDAOImpl;
import com.natour.server.data.dao.implemented.ImageDAOImpl;

@Configuration
public class AwsS3Config {
	
	
	
	@Value("${amazon.aws.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.secretkey}")
    private String secretKey;

	@Value("${amazon.s3.region}")
    private String region;

	
	@Bean
	public AmazonS3 amazonS3() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
	    
	   AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
	    	.withCredentials(new AWSStaticCredentialsProvider(credentials))
	    	.withRegion(region)
	    	.build();
	   
	   return amazonS3;
	}
	
	@Bean
	public ImageDAOImpl imageDAOImpl() {
		return new ImageDAOImpl();
	}
	
	@Bean
	public GpxDAOImpl gpxDAOImpl() {
		return new GpxDAOImpl();
	}
	
}
