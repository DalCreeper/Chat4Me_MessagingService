package com.advancia.chat4me_messaging_service.domain.services;

import com.advancia.chat4me_messaging_service.domain.model.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<Message> getMessages (UUID userIdSender, UUID userIdReceiver);
}
