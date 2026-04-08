package com.dealership.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Dealership REST API.
 *
 * Run with: mvn spring-boot:run
 * Or build: mvn clean package  → java -jar target/dealership-api-*.jar
 */
@SpringBootApplication
public class DealershipApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DealershipApiApplication.class, args);
    }
}
