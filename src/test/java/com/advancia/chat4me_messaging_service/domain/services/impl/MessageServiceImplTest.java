package com.advancia.chat4me_messaging_service.domain.services.impl;

import com.advancia.chat4me_messaging_service.domain.model.Message;
import com.advancia.chat4me_messaging_service.domain.model.NewMessage;
import com.advancia.chat4me_messaging_service.domain.repository.MessagesRepoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceImplTest {
    @Mock
    private MessagesRepoService messagesRepoService;
    @InjectMocks
    private MessageServiceImpl messageServiceImpl;

    @Test
    void shouldReturnMessages_whenIsAllOk() {
        String tokenSender = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik";
        UUID userIdReceiver = UUID.randomUUID();
        List<Message> messages = List.of(
            Message.builder()
                .id(UUID.randomUUID())
                .tokenSender(tokenSender)
                .receiver(userIdReceiver)
                .content("content")
                .received(false)
                .timestamp(OffsetDateTime.now())
                .build(),
            Message.builder()
                .id(UUID.randomUUID())
                .tokenSender(tokenSender)
                .receiver(userIdReceiver)
                .content("content2")
                .received(false)
                .timestamp(OffsetDateTime.now())
                .build()
        );

        doReturn(messages).when(messagesRepoService).getMessages(tokenSender, userIdReceiver);

        List<Message> result = messageServiceImpl.getMessages(tokenSender, userIdReceiver);
        assertEquals(messages, result);

        verify(messagesRepoService).getMessages(tokenSender, userIdReceiver);
    }

    @Test
    void shouldPropagateException_whenMessagesRepoServiceFails() {
        String tokenSender = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik";
        UUID userIdReceiver = UUID.randomUUID();
        RuntimeException runtimeException = new RuntimeException("Service error");

        doThrow(runtimeException).when(messagesRepoService).getMessages(tokenSender, userIdReceiver);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> messageServiceImpl.getMessages(tokenSender, userIdReceiver));
        assertSame(runtimeException, ex);

        verify(messagesRepoService).getMessages(tokenSender, userIdReceiver);
    }

    @Test
    void shouldReturnNewMessage_whenIsAllOk() {
        NewMessage newMessage = new NewMessage(
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik",
            UUID.randomUUID(),
            "test"
        );
        Message savedMessage = Message.builder()
            .id(UUID.randomUUID())
            .tokenSender(newMessage.getTokenSender())
            .receiver(newMessage.getReceiver())
            .content(newMessage.getContent())
            .received(false)
            .timestamp(OffsetDateTime.now())
            .build();

        doReturn(savedMessage).when(messagesRepoService).newMessage(newMessage);

        Message result = messageServiceImpl.newMessage(newMessage);
        assertEquals(savedMessage, result);

        verify(messagesRepoService).newMessage(newMessage);
    }

    @Test
    void shouldPropagateException_whenNewMessageRepoServiceFails() {
        NewMessage newMessage = new NewMessage(
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3ZjExM2JiMi0zOGViLTQ3ZTctODRhMi1jZjI3MDMwMDRiODYiLCJpYXQiOjE3NDExMDczMDAsImV4cCI6MTc0MTE5MzcwMH0.lVCPs_piZa-se2ABiy6xjfor5oAvKSvv1T_n5YYKnik",
            UUID.randomUUID(),
            "test"
        );
        RuntimeException runtimeException = new RuntimeException("Service error");

        doThrow(runtimeException).when(messagesRepoService).newMessage(newMessage);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> messageServiceImpl.newMessage(newMessage));
        assertSame(runtimeException, ex);

        verify(messagesRepoService).newMessage(newMessage);
    }
}