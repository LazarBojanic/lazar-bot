package com.lazar.lazarwordleclonebackendspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableJpaRepositories
@EnableMongoRepositories
@SpringBootApplication
public class LazarWordleCloneBackendSpringApplication {
	public static void main(String[] args) {
        SpringApplication.run(LazarWordleCloneBackendSpringApplication.class, args);
    }
}
