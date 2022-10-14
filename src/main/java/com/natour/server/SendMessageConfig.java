package com.natour.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.natour.server.data.dao.implemented.SendMessageDAOImpl;

@Configuration
public class SendMessageConfig {

	@Bean
	SendMessageDAOImpl sendMessageDAOImpl() {
		return new SendMessageDAOImpl();
	}
}
