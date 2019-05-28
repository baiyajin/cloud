package com.baiyajin.report;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@MapperScan(value = "com.baiyajin.report.mapper")
@SpringBootApplication
@EnableScheduling
public class ReportApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReportApplication.class, args);
	}
}
