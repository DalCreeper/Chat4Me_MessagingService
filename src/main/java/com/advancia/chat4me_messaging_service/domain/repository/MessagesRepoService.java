package com.advancia.chat4me_messaging_service.domain.repository;

import com.advancia.chat4me_messaging_service.domain.model.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MessagesRepoService {
    List<Message> getMessages(UUID uuidSender, UUID uuidReceiver);
}
