package com.advancia.chat4me_messaging_service.application.services;

import com.advancia.Chat4Me_Messaging_Service.generated.application.model.MessageDto;
import com.advancia.Chat4Me_Messaging_Service.generated.application.model.NewMessageDto;
import com.advancia.chat4me_messaging_service.application.mappers.MessageMappers;
import com.advancia.chat4me_messaging_service.domain.model.Message;
import com.advancia.chat4me_messaging_service.domain.model.NewMessage;
import com.advancia.chat4me_messaging_service.domain.services.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessagingApiDelegateImplTest {
    //private MessageService messageService = mock(MessageService.class);
    @Mock
    private MessageService messageService;
    @Mock
    private MessageMappers messageMappers;
    @InjectMocks
    private MessagingApiDelegateImpl messagingApiDelegateImpl;

    @Test
    void shouldReturnMessages_whenAllOk() {
        UUID userIdSender = UUID.randomUUID();
        UUID userIdReceiver = UUID.randomUUID();
        List<Message> messages = List.of(
            Message.builder().id(UUID.randomUUID()).build(),
            Message.builder().id(UUID.randomUUID()).build()
        );
        List<MessageDto> messagesDto = List.of(
            new MessageDto().id(messages.get(0).getId()),
            new MessageDto().id(messages.get(1).getId())
        );

        doReturn(messages).when(messageService).getMessages(userIdSender, userIdReceiver);
        doReturn(messagesDto).when(messageMappers).convertFromDomain(messages);

        ResponseEntity<List<MessageDto>> response = messagingApiDelegateImpl.getMessages(userIdSender, userIdReceiver);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(messagesDto, response.getBody());

        verify(messageService).getMessages(userIdSender, userIdReceiver);
        //verify(messageService, times(2)).getMessages(userIdSender, userIdReceiver);
        //verify(messageService, never()).getMessages(userIdSender, userIdReceiver);
        verify(messageMappers).convertFromDomain(messages);
    }

    @Test
    void shouldPropagateException_whenMessageServiceFails() {
        UUID userIdSender = UUID.randomUUID();
        UUID userIdReceiver = UUID.randomUUID();
        RuntimeException runtimeException = new RuntimeException("Service error");

        doThrow(runtimeException).when(messageService).getMessages(userIdSender, userIdReceiver);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> messagingApiDelegateImpl.getMessages(userIdSender, userIdReceiver));
        assertSame(runtimeException, ex);

        verify(messageService).getMessages(userIdSender, userIdReceiver);
        verify(messageMappers, never()).convertFromDomain(anyList());
    }

    @Test
    void shouldPropagateException_whenMessageMappersFails() {
        UUID userIdSender = UUID.randomUUID();
        UUID userIdReceiver = UUID.randomUUID();
        List<Message> messages = List.of(
            Message.builder().id(UUID.randomUUID()).build(),
            Message.builder().id(UUID.randomUUID()).build()
        );
        RuntimeException runtimeException = new RuntimeException("Mapping error");

        doReturn(messages).when(messageService).getMessages(userIdSender, userIdReceiver);
        doThrow(runtimeException).when(messageMappers).convertFromDomain(messages);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> messagingApiDelegateImpl.getMessages(userIdSender, userIdReceiver));
        assertSame(runtimeException, ex);

        verify(messageService).getMessages(userIdSender, userIdReceiver);
        verify(messageMappers).convertFromDomain(messages);
    }

    @Test
    void shouldReturnNewMessage_whenAllOk() {
        NewMessageDto newMessageDto = new NewMessageDto()
            .sender(UUID.randomUUID())
            .receiver(UUID.randomUUID())
            .content("test");
        NewMessage newMess = new NewMessage();
        Message newMessage = Message.builder().id(UUID.randomUUID()).build();
        MessageDto newMessDto = new MessageDto().id(newMessage.getId());
        URI location = URI.create("/messages/" + newMessage.getId());

        doReturn(newMess).when(messageMappers).convertToDomain(newMessageDto);
        doReturn(newMessage).when(messageService).newMessage(newMess);
        doReturn(newMessDto).when(messageMappers).convertFromDomain(newMessage);

        ResponseEntity<MessageDto> response = messagingApiDelegateImpl.newMessage(newMessageDto);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(newMessDto, response.getBody());
        assertEquals(location.toString(), Objects.requireNonNull(response.getHeaders().getLocation()).toString());

        verify(messageService).newMessage(newMess);
        verify(messageMappers).convertToDomain(newMessageDto);
        verify(messageMappers).convertFromDomain(newMessage);
    }

    @Test
    void shouldPropagateException_whenNewMessageServiceFails() {
        NewMessageDto newMessageDto = new NewMessageDto()
            .sender(UUID.randomUUID())
            .receiver(UUID.randomUUID())
            .content("test");
        RuntimeException runtimeException = new RuntimeException("Service error");
        NewMessage newMessage = new NewMessage();

        doReturn(newMessage).when(messageMappers).convertToDomain(newMessageDto);

        doThrow(runtimeException).when(messageService).newMessage(newMessage);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> messagingApiDelegateImpl.newMessage(newMessageDto));
        assertSame(runtimeException, ex);

        verify(messageMappers).convertToDomain(newMessageDto);
        verify(messageService).newMessage(newMessage);
        verify(messageMappers, never()).convertFromDomain(any(Message.class));
    }

    @Test
    void shouldPropagateException_whenNewMessageMappersFails() {
        NewMessageDto newMessageDto = new NewMessageDto()
            .sender(UUID.randomUUID())
            .receiver(UUID.randomUUID())
            .content("test");
        RuntimeException runtimeException = new RuntimeException("Mapping error");
        NewMessage newMessage = new NewMessage();

        doThrow(runtimeException).when(messageMappers).convertToDomain(newMessageDto);

        Exception ex = assertThrowsExactly(RuntimeException.class, () -> messagingApiDelegateImpl.newMessage(newMessageDto));
        assertSame(runtimeException, ex);

        verify(messageMappers).convertToDomain(newMessageDto);
        verify(messageService, never()).newMessage(newMessage);
        verify(messageMappers, never()).convertFromDomain(any(Message.class));
    }
}