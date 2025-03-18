package com.advancia.chat4me_messaging_service.infrastructure.mappers;

import com.advancia.chat4me_messaging_service.domain.model.Message;
import com.advancia.chat4me_messaging_service.domain.model.NewMessage;
import com.advancia.chat4me_messaging_service.infrastructure.model.MessageEntity;
import com.advancia.chat4me_messaging_service.infrastructure.model.NewMessageEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MessageEntityMappersImplTest {
    @InjectMocks
    private MessageEntityMappersImpl messageEntityMappersImpl;

    @Test
    void shouldConvertMessageEntityFromInfrastructure_whenIsAllOk() {
        MessageEntity messageEntity = MessageEntity.builder()
            .id(UUID.randomUUID())
            .sender(UUID.randomUUID())
            .receiver(UUID.randomUUID())
            .content("content")
            .received(false)
            .timestamp(OffsetDateTime.now())
            .build();

        Message message = messageEntityMappersImpl.convertFromInfrastructure(messageEntity);
        assertNotNull(message);
        assertEquals(messageEntity.getId(), message.getId());
        assertEquals(messageEntity.getSender(), message.getSender());
        assertEquals(messageEntity.getReceiver(), message.getReceiver());
        assertEquals(messageEntity.getContent(), message.getContent());
        assertEquals(messageEntity.getReceived(), message.getReceived());
        assertEquals(messageEntity.getTimestamp(), message.getTimestamp());
    }

    @Test
    void shouldReturnNull_whenMessageEntityIsNull() {
        assertNull(messageEntityMappersImpl.convertFromInfrastructure((MessageEntity) null));
    }

    @Test
    void shouldConvertListOfMessageEntitiesFromInfrastructure_whenIsAllOk() {
        List<MessageEntity> messagesEntity = List.of(
            MessageEntity.builder()
                .id(UUID.randomUUID())
                .sender(UUID.randomUUID())
                .receiver(UUID.randomUUID())
                .content("content")
                .received(false)
                .timestamp(OffsetDateTime.now())
                .build(),
            MessageEntity.builder()
                .id(UUID.randomUUID())
                .sender(UUID.randomUUID())
                .receiver(UUID.randomUUID())
                .content("content2")
                .received(false)
                .timestamp(OffsetDateTime.now())
                .build()
        );

        List<Message> messages = messageEntityMappersImpl.convertFromInfrastructure(messagesEntity);
        assertNotNull(messages);
        assertEquals(2, messages.size());
        assertEquals(messagesEntity.get(0).getId(), messages.get(0).getId());
        assertEquals(messagesEntity.get(1).getId(), messages.get(1).getId());
    }

    @Test
    void shouldReturnEmptyList_whenMessageEntityListIsEmpty() {
        List<MessageEntity> messagesEntity = List.of();

        List<Message> messages = messageEntityMappersImpl.convertFromInfrastructure(messagesEntity);
        assertNotNull(messages);
        assertTrue(messages.isEmpty());
    }

    @Test
    void shouldReturnNull_whenMessageEntityListIsNull() {
        assertNull(messageEntityMappersImpl.convertFromInfrastructure((List<MessageEntity>) null));
    }

    @Test
    void shouldConvertNewMessageToInfrastructure_whenIsAllOk() {
        NewMessage newMessage = NewMessage.builder()
            .sender(UUID.randomUUID())
            .receiver(UUID.randomUUID())
            .content("test")
            .build();

        NewMessageEntity newMessageEntity = messageEntityMappersImpl.convertToInfrastructure(newMessage);
        assertNotNull(newMessageEntity);
        assertEquals(newMessage.getSender(), newMessageEntity.getSender());
        assertEquals(newMessage.getReceiver(), newMessageEntity.getReceiver());
        assertEquals(newMessage.getContent(), newMessageEntity.getContent());
    }

    @Test
    void shouldReturnNull_whenNewMessageIsNull() {
        assertNull(messageEntityMappersImpl.convertToInfrastructure((NewMessage) null));
    }
}