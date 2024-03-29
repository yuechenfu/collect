package com.sike;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sike.collect.dao")
public class CollectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollectApplication.class, args);
	}

}
