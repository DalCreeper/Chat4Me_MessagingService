package com.advancia.chat4me_messaging_service.application.mappers;

import com.advancia.Chat4Me_Messaging_Service.generated.application.model.MessageDto;
import com.advancia.chat4me_messaging_service.domain.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface MessageMappers {
    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "receiver", target = "receiver")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "received", target = "received")
    //@Mapping(source = "timestamp", target = "timestamp")
    MessageDto convertFromDomain(Message message);
    List<MessageDto> convertFromDomain(List<Message> messages);
}
