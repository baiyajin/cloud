package com.baiyajin.materials;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

//@EnableCaching
@MapperScan(value = "com.baiyajin.materials.mapper")
@SpringBootApplication
public class MaterialsApplication {
	public static void main(String[] args) {
		SpringApplication.run(MaterialsApplication.class, args);
	}
}
