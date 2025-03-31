package com.advancia.chat4me_messaging_service.infrastructure.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class NewMessageEntity {
    private UUID sender;
    private UUID receiver;
    private String content;
}