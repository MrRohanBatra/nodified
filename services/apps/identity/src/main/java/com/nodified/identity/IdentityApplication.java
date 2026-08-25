package com.nodified.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class IdentityApplication {
    public static void main(String[] args) {
        // log.info("Hello from the Identity Application");
        SpringApplication.run(IdentityApplication.class, args);
    }
}
