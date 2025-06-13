package com.advancia.chat4me_messaging_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class Chat4MeMessagingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(Chat4MeMessagingServiceApplication.class, args);
    }
}