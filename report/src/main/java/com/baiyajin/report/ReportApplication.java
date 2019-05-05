package com.baiyajin.report;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableCaching
@MapperScan(value = "com.baiyajin.report.mapper")
@SpringBootApplication
public class ReportApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReportApplication.class, args);
	}
}
