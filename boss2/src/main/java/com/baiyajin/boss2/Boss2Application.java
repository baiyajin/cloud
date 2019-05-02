package com.baiyajin.boss2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class Boss2Application {

	public static void main(String[] args) {
		SpringApplication.run(Boss2Application.class, args);
	}

}
