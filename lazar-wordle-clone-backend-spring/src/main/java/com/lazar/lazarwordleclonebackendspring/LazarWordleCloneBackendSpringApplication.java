package com.lazar.lazarwordleclonebackendspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@EnableMongoRepositories
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LazarWordleCloneBackendSpringApplication {
	public static void main(String[] args) {
        SpringApplication.run(LazarWordleCloneBackendSpringApplication.class, args);
    }
}
