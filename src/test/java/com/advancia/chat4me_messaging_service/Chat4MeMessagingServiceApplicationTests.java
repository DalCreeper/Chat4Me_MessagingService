package com.advancia.chat4me_messaging_service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Chat4MeMessagingServiceApplicationTests {
    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> Chat4MeMessagingServiceApplication.main(new String[]{}));
    }
}