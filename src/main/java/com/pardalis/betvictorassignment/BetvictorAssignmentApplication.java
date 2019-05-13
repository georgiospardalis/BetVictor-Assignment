package com.pardalis.betvictorassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableJms
@EnableWebSocketMessageBroker
public class BetvictorAssignmentApplication {
	public static void main(String[] args) {
		SpringApplication.run(BetvictorAssignmentApplication.class, args);
	}
}