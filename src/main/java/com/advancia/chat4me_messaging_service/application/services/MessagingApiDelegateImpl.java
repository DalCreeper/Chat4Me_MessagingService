package com.advancia.chat4me_messaging_service.application.services;

import com.advancia.Chat4Me_Messaging_Service.generated.application.api.MessagingApiDelegate;
import com.advancia.Chat4Me_Messaging_Service.generated.application.model.MessageDto;
import com.advancia.Chat4Me_Messaging_Service.generated.application.model.NewMessageDto;
import com.advancia.chat4me_messaging_service.application.mappers.MessageMappers;
import com.advancia.chat4me_messaging_service.domain.model.Message;
import com.advancia.chat4me_messaging_service.domain.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessagingApiDelegateImpl implements MessagingApiDelegate {
    private final MessageService messageService;
    private final MessageMappers messageMappers;

    @Override
    public ResponseEntity<List<MessageDto>> getMessages(String tokenSender, UUID userIdReceiver) {
        List<Message> messages = messageService.getMessages(tokenSender, userIdReceiver);
        List<MessageDto> messagesDto = messageMappers.convertFromDomain(messages);
        return ResponseEntity.ok(messagesDto);
    }

    @Override
    public ResponseEntity<MessageDto> newMessage(NewMessageDto newMessageDto) {
        Message newMessage = messageService.newMessage(messageMappers.convertToDomain(newMessageDto));
        MessageDto newMessDto = messageMappers.convertFromDomain(newMessage);
        URI location = URI.create("/messages/" + newMessage.getId());
        return ResponseEntity.created(location).body(newMessDto);
    }
}