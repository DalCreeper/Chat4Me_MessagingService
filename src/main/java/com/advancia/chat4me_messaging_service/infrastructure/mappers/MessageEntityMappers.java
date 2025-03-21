package com.advancia.chat4me_messaging_service.infrastructure.mappers;

import com.advancia.chat4me_messaging_service.domain.model.Message;
import com.advancia.chat4me_messaging_service.domain.model.NewMessage;
import com.advancia.chat4me_messaging_service.infrastructure.model.MessageEntity;
import com.advancia.chat4me_messaging_service.infrastructure.model.NewMessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface MessageEntityMappers {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "tokenSender", target = "tokenSender")
    @Mapping(source = "receiver", target = "receiver")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "received", target = "received")
    @Mapping(source = "timestamp", target = "timestamp")
    Message convertFromInfrastructure(MessageEntity messageEntity);
    List<Message> convertFromInfrastructure(List<MessageEntity> messagesEntity);

    @Mapping(source = "tokenSender", target = "tokenSender")
    @Mapping(source = "receiver", target = "receiver")
    @Mapping(source = "content", target = "content")
    NewMessageEntity convertToInfrastructure(NewMessage newMessage);
}
