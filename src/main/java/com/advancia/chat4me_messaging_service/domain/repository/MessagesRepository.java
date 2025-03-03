package com.advancia.chat4me_messaging_service.domain.repository;

import com.advancia.chat4me_messaging_service.domain.model.Message;
import feign.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessagesRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT m FROM Message m " +
           "WHERE m.sender = :userIdSender " +
           "AND (:userIdReceiver IS NULL OR m.receiver = :userIdReceiver)")
    List<Message> getMessages(@Param("userIdSender") UUID userIdSender,
                              @Param("userIdReceiver") UUID userIdReceiver);

    @NonNull
    Optional<Message> findById(@NonNull UUID id);
}
