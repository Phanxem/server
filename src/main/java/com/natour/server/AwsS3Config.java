package com.natour.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsS3Config {
	
	/*
	
	@Value("${amazon.aws.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.secretkey}")
    private String secretKey;

	@Value("${amazon.s3.region}")
    private String region;
*/
	
	
	/*
	@Bean
	public AmazonS3 getAmazonS3Client() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
	    

	   AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
	    	.withCredentials(new AWSStaticCredentialsProvider(credentials))
	    	.withRegion("sed")
	    	.build();
	   
	   return amazonS3;
	}
	*/
}
