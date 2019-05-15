package com.baiyajin.materials;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableCaching
@MapperScan(value = "com.baiyajin.materials.mapper")
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class MaterialsApplication {
	public static void main(String[] args) {
		SpringApplication.run(MaterialsApplication.class, args);
	}
}
