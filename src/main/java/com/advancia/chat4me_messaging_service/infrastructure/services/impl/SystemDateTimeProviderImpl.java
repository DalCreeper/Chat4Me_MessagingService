package com.advancia.chat4me_messaging_service.infrastructure.services.impl;

import com.advancia.chat4me_messaging_service.infrastructure.services.SystemDateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class SystemDateTimeProviderImpl implements SystemDateTimeProvider {
    @Override
    public OffsetDateTime now() {
        return OffsetDateTime.now();
    }
}