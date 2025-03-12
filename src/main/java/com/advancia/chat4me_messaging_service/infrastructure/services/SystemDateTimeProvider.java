package com.advancia.chat4me_messaging_service.infrastructure.services;

import java.time.OffsetDateTime;

public interface SystemDateTimeProvider {
    OffsetDateTime now();
}