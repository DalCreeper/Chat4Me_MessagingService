package com.advancia.chat4me_messaging_service.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessagingServiceExceptionTest {
    @Test
    void shouldThrowAnExceptionWithMessage_whenIsAllOk() {
        String errorMessage = "Retrieve messages failed";
        MessagingServiceException messagingServiceException = new MessagingServiceException(errorMessage);

        assertEquals(errorMessage, messagingServiceException.getMessage());
    }

    @Test
    void shouldThrowAnExceptionWithMessageAndCause_whenIsAllOk() {
        String errorMessage = "An error occurred while retrieving messages";
        Throwable cause = new RuntimeException("Root cause");
        MessagingServiceException messagingServiceException = new MessagingServiceException(errorMessage, cause);

        assertEquals(errorMessage, messagingServiceException.getMessage());
        assertEquals(cause, messagingServiceException.getCause());
    }
}