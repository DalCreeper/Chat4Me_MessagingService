package com.advancia.chat4me_messaging_service.domain.exceptions;

public class MessagingServiceException extends RuntimeException {
    public MessagingServiceException(String message) {
        super(message);
    }

    public MessagingServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}