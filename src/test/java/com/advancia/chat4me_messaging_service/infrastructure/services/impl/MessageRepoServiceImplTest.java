package com.advancia.chat4me_messaging_service.infrastructure.services.impl;

import com.advancia.chat4me_messaging_service.domain.model.Message;
import com.advancia.chat4me_messaging_service.domain.model.NewMessage;
import com.advancia.chat4me_messaging_service.infrastructure.mappers.MessageEntityMappers;
import com.advancia.chat4me_messaging_service.infrastructure.model.MessageEntity;
import com.advancia.chat4me_messaging_service.infrastructure.model.NewMessageEntity;
import com.advancia.chat4me_messaging_service.infrastructure.repository.MessagesRepository;
import com.advancia.chat4me_messaging_service.infrastructure.services.SystemDateTimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageRepoServiceImplTest {
    @Mock
    private MessagesRepository messagesRepository;
    @Mock
    private MessageEntityMappers messageEntityMappers;
    @Mock
    private SystemDateTimeProvider systemDateTimeProvider;
    @InjectMocks
    private MessagesRepoServiceImpl messagesRepoServiceImpl;

    @Test
    void shouldSetReceivedAndReturnMessages_whenIsAllOk() {
        String tokenSender = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik";
        UUID userIdReceiver = UUID.randomUUID();
        List<MessageEntity> messagesEntity = List.of(
            MessageEntity.builder()
                .id(UUID.randomUUID())
                .tokenSender(tokenSender)
                .receiver(userIdReceiver)
                .content("content")
                .received(false)
                .timestamp(OffsetDateTime.now())
                .build(),
            MessageEntity.builder()
                .id(UUID.randomUUID())
                .tokenSender(tokenSender)
                .receiver(userIdReceiver)
                .content("content2")
                .received(false)
                .timestamp(OffsetDateTime.now())
                .build()
        );
        List<Message> messages = List.of(
            Message.builder()
                .id(messagesEntity.get(0).getId())
                .tokenSender(messagesEntity.get(0).getTokenSender())
                .receiver(messagesEntity.get(0).getReceiver())
                .content(messagesEntity.get(0).getContent())
                .received(messagesEntity.get(0).getReceived())
                .timestamp(messagesEntity.get(0).getTimestamp())
                .build(),
            Message.builder()
                .id(messagesEntity.get(1).getId())
                .tokenSender(messagesEntity.get(1).getTokenSender())
                .receiver(messagesEntity.get(1).getReceiver())
                .content(messagesEntity.get(1).getContent())
                .received(messagesEntity.get(1).getReceived())
                .timestamp(messagesEntity.get(1).getTimestamp())
                .build()
        );

        doReturn(messagesEntity).when(messagesRepository).getMessages(tokenSender, userIdReceiver);
        doReturn(messages).when(messageEntityMappers).convertFromInfrastructure(messagesEntity);

        List<Message> messagesResult = messagesRepoServiceImpl.getMessages(tokenSender, userIdReceiver);

        assertEquals(messages, messagesResult);
        assertTrue(messagesEntity.stream().allMatch(MessageEntity::getReceived));

        verify(messagesRepository).saveAll(messagesEntity);
        verify(messagesRepository).getMessages(tokenSender, userIdReceiver);
        verify(messageEntityMappers).convertFromInfrastructure(messagesEntity);
    }

    @Test
    void shouldPropagateException_whenMessagesRepositoryFails() {
        String tokenSender = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik";
        UUID userIdReceiver = UUID.randomUUID();
        RuntimeException runtimeException = new RuntimeException("Repository error");

        doThrow(runtimeException).when(messagesRepository).getMessages(tokenSender, userIdReceiver);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> messagesRepoServiceImpl.getMessages(tokenSender, userIdReceiver));
        assertSame(runtimeException, ex);

        verify(messagesRepository).getMessages(tokenSender, userIdReceiver);
        verify(messageEntityMappers, never()).convertFromInfrastructure(anyList());
    }

    @Test
    void shouldSaveAndReturnNewMessage_whenIsAllOk() {
        NewMessage newMessage = NewMessage.builder()
            .tokenSender("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .receiver(UUID.randomUUID())
            .content("test")
            .build();
        NewMessageEntity newMessageEntity = NewMessageEntity.builder()
            .tokenSender(newMessage.getTokenSender())
            .receiver(newMessage.getReceiver())
            .content(newMessage.getContent())
            .build();

        doReturn(newMessageEntity).when(messageEntityMappers).convertToInfrastructure(newMessage);

        OffsetDateTime fixedDateTime = OffsetDateTime.parse("2025-03-12T12:00:00.174779800+01:00");
        doReturn(fixedDateTime).when(systemDateTimeProvider).now();

        MessageEntity savedMessage = MessageEntity.builder()
            .tokenSender(newMessageEntity.getTokenSender())
            .receiver(newMessageEntity.getReceiver())
            .content(newMessageEntity.getContent())
            .received(false)
            .timestamp(fixedDateTime)
            .build();
        doReturn(savedMessage).when(messagesRepository).save(savedMessage);

        Message message = Message.builder()
            .tokenSender(savedMessage.getTokenSender())
            .receiver(savedMessage.getReceiver())
            .content(savedMessage.getContent())
            .build();
        doReturn(message).when(messageEntityMappers).convertFromInfrastructure(savedMessage);

        Message messageResult = messagesRepoServiceImpl.newMessage(newMessage);
        assertEquals(message, messageResult);

        verify(messageEntityMappers).convertToInfrastructure(newMessage);
        verify(messagesRepository).save(savedMessage);
        verify(messageEntityMappers).convertFromInfrastructure(savedMessage);
    }

    @Test
    void shouldPropagateException_whenNewMessageRepositoryFails() {
        NewMessage newMessage = NewMessage.builder()
            .tokenSender("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik")
            .receiver(UUID.randomUUID())
            .content("test")
            .build();
        NewMessageEntity newMessageEntity = NewMessageEntity.builder()
            .tokenSender(newMessage.getTokenSender())
            .receiver(newMessage.getReceiver())
            .content(newMessage.getContent())
            .build();

        doReturn(newMessageEntity).when(messageEntityMappers).convertToInfrastructure(newMessage);

        OffsetDateTime fixedDateTime = OffsetDateTime.parse("2025-03-12T12:00:00.174779800+01:00");
        doReturn(fixedDateTime).when(systemDateTimeProvider).now();

        MessageEntity savedMessage = MessageEntity.builder()
            .tokenSender(newMessageEntity.getTokenSender())
            .receiver(newMessageEntity.getReceiver())
            .content(newMessageEntity.getContent())
            .received(false)
            .timestamp(fixedDateTime)
            .build();
        RuntimeException runtimeException = new RuntimeException("Repository error");

        doThrow(runtimeException).when(messagesRepository).save(savedMessage);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> messagesRepoServiceImpl.newMessage(newMessage));
        assertSame(runtimeException, ex);

        verify(messageEntityMappers).convertToInfrastructure(newMessage);
        verify(messagesRepository).save(savedMessage);
        verify(messageEntityMappers, never()).convertFromInfrastructure(any(MessageEntity.class));
    }
}