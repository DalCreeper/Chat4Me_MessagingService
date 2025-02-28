package com.advancia.chat4me_messaging_service.domain.services.impl;

import com.advancia.chat4me_messaging_service.domain.model.Message;
import com.advancia.chat4me_messaging_service.domain.repository.MessagesRepoService;
import com.advancia.chat4me_messaging_service.domain.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessagesRepoService messagesRepoService;

    @Override
    public List<Message> getMessages(UUID userIdSender, UUID userIdReceiver) {
        return messagesRepoService.getMessages(userIdSender, userIdReceiver);
    }
}