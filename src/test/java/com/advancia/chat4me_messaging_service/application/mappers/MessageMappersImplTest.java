package com.advancia.chat4me_messaging_service.application.mappers;

import com.advancia.chat4me_messaging_service.generated.application.model.MessageDto;
import com.advancia.chat4me_messaging_service.generated.application.model.NewMessageDto;
import com.advancia.chat4me_messaging_service.domain.model.Message;
import com.advancia.chat4me_messaging_service.domain.model.NewMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MessageMappersImplTest {
    @InjectMocks
    private MessageMappersImpl messageMappersImpl;

    @Test
    void shouldConvertMessageFromDomain_whenIsAllOk() {
        Message message = Message.builder()
            .id(UUID.randomUUID())
            .sender(UUID.randomUUID())
            .receiver(UUID.randomUUID())
            .content("content")
            .received(false)
            .timestamp(OffsetDateTime.now())
            .build();

        MessageDto messageDto = messageMappersImpl.convertFromDomain(message);
        assertNotNull(messageDto);
        assertEquals(message.getId(), messageDto.getId());
        assertEquals(message.getSender(), messageDto.getSender());
        assertEquals(message.getReceiver(), messageDto.getReceiver());
        assertEquals(message.getContent(), messageDto.getContent());
        assertEquals(message.getReceived(), messageDto.getReceived());
        assertEquals(message.getTimestamp(), messageDto.getTimestamp());
    }

    @Test
    void shouldReturnNull_whenMessageIsNull() {
        assertNull(messageMappersImpl.convertFromDomain((Message) null));
    }

    @Test
    void shouldConvertListOfMessagesFromDomain_whenIsAllOk() {
        List<Message> messages = List.of(
            Message.builder()
                .id(UUID.randomUUID())
                .sender(UUID.randomUUID())
                .receiver(UUID.randomUUID())
                .content("content")
                .received(false)
                .timestamp(OffsetDateTime.now())
                .build(),
            Message.builder()
                .id(UUID.randomUUID())
                .sender(UUID.randomUUID())
                .receiver(UUID.randomUUID())
                .content("content2")
                .received(false)
                .timestamp(OffsetDateTime.now())
                .build()
        );

        List<MessageDto> messagesDto = messageMappersImpl.convertFromDomain(messages);
        assertNotNull(messagesDto);
        assertEquals(2, messagesDto.size());
        assertEquals(messages.get(0).getId(), messagesDto.get(0).getId());
        assertEquals(messages.get(1).getId(), messagesDto.get(1).getId());
    }

    @Test
    void shouldReturnEmptyList_whenMessageListIsEmpty() {
        List<Message> messages = List.of();

        List<MessageDto> messagesDto = messageMappersImpl.convertFromDomain(messages);
        assertNotNull(messagesDto);
        assertTrue(messagesDto.isEmpty());
    }

    @Test
    void shouldReturnNull_whenMessageListIsNull() {
        assertNull(messageMappersImpl.convertFromDomain((List<Message>) null));
    }

    @Test
    void shouldConvertNewMessageToDomain_whenIsAllOk() {
        NewMessageDto newMessageDto = new NewMessageDto()
            .sender(UUID.randomUUID())
            .receiver(UUID.randomUUID())
            .content("test");

        NewMessage newMessage = messageMappersImpl.convertToDomain(newMessageDto);
        assertNotNull(newMessage);
        assertEquals(newMessageDto.getSender(), newMessage.getSender());
        assertEquals(newMessageDto.getReceiver(), newMessage.getReceiver());
        assertEquals(newMessageDto.getContent(), newMessage.getContent());
    }

    @Test
    void shouldReturnNull_whenNewMessageDtoIsNull() {
        assertNull(messageMappersImpl.convertToDomain((NewMessageDto) null));
    }
}