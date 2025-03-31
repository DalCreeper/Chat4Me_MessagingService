package com.advancia.chat4me_messaging_service.infrastructure.services.impl;

import com.advancia.chat4me_messaging_service.domain.model.Message;
import com.advancia.chat4me_messaging_service.domain.model.NewMessage;
import com.advancia.chat4me_messaging_service.domain.repository.MessagesRepoService;
import com.advancia.chat4me_messaging_service.infrastructure.mappers.MessageEntityMappers;
import com.advancia.chat4me_messaging_service.infrastructure.model.MessageEntity;
import com.advancia.chat4me_messaging_service.infrastructure.model.NewMessageEntity;
import com.advancia.chat4me_messaging_service.infrastructure.repository.MessagesRepository;
import com.advancia.chat4me_messaging_service.infrastructure.services.SystemDateTimeProvider;
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
    private final MessageEntityMappers messageEntityMappers;
    private final SystemDateTimeProvider systemDateTimeProvider;

    @Override
    public List<Message> getMessages(UUID userIdSender, UUID userIdReceiver) {
        List<MessageEntity> messagesEntity = messagesRepository.getMessages(userIdSender, userIdReceiver);
        messagesEntity.forEach(msg -> msg.setReceived(true));
        messagesRepository.saveAll(messagesEntity);
        return messageEntityMappers.convertFromInfrastructure(messagesEntity);
    }

    @Override
    public Message newMessage(NewMessage newMessage) {
        NewMessageEntity newMessageEntity = messageEntityMappers.convertToInfrastructure(newMessage);
        OffsetDateTime now = systemDateTimeProvider.now();
        MessageEntity savedMessage = buildMessageEntity(newMessageEntity, now);
        return messageEntityMappers.convertFromInfrastructure(messagesRepository.save(savedMessage));
    }

    private MessageEntity buildMessageEntity(NewMessageEntity newMessageEntity, OffsetDateTime now) {
        return MessageEntity.builder()
            .sender(newMessageEntity.getSender())
            .receiver(newMessageEntity.getReceiver())
            .content(newMessageEntity.getContent())
            .received(false)
            .timestamp(now)
            .build();
    }
}