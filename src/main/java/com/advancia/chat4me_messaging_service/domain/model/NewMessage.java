package com.advancia.chat4me_messaging_service.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "new_message")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class NewMessage {

    @Id
    private UUID id;
    private UUID sender;
    private UUID receiver;
    private String content;
}