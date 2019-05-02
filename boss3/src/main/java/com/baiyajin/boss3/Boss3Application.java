package com.baiyajin.boss3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class Boss3Application {

	public static void main(String[] args) {
		SpringApplication.run(Boss3Application.class, args);
	}

}
