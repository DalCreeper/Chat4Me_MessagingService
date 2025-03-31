package com.advancia.chat4me_messaging_service.infrastructure.repository;

import com.advancia.chat4me_messaging_service.infrastructure.model.MessageEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessagesRepository extends JpaRepository<MessageEntity, UUID> {

    @Query("SELECT m FROM MessageEntity m " +
           "WHERE m.sender = :userIdSender " +
           "AND (:userIdReceiver IS NULL OR m.receiver = :userIdReceiver)")
    List<MessageEntity> getMessages(
        @Param("userIdSender") UUID userIdSender,
        @Param("userIdReceiver") UUID userIdReceiver
    );

    @NonNull
    Optional<MessageEntity> findById(@NonNull UUID id);
}