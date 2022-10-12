package com.natour.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;



@SpringBootApplication
@EntityScan({"com.natour.server.data.entities.dynamoDB", "com.natour.server.data.entities.rds" })
public class ServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		
		
		
		System.out.println("START");
	}

}
