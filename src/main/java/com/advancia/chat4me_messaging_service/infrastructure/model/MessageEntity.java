package com.advancia.chat4me_messaging_service.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID sender;
    private UUID receiver;
    private String content;
    private Boolean received;
    private OffsetDateTime timestamp;
}