package com.advancia.chat4me_messaging_service.infrastructure.services.impl;

import com.advancia.chat4me_messaging_service.domain.model.Message;
import com.advancia.chat4me_messaging_service.domain.model.NewMessage;
import com.advancia.chat4me_messaging_service.domain.repository.MessagesRepoService;
import com.advancia.chat4me_messaging_service.domain.repository.MessagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class MessagesRepoServiceImpl implements MessagesRepoService {
    private final MessagesRepository messagesRepository;

    @Override
    public List<Message> getMessages(UUID userIdSender, UUID userIdReceiver) {
        return messagesRepository.getMessages(userIdSender, userIdReceiver);
    }

    @Override
    public Message newMessage(NewMessage newMessage) {
        OffsetDateTime now = OffsetDateTime.now();
        Message savedMessage = Message.builder()
            .sender(newMessage.getSender())
            .receiver(newMessage.getReceiver())
            .content(newMessage.getContent())
            .received(false)
            .timestamp(now)
            .build();

        return messagesRepository.save(savedMessage);
    }
}
