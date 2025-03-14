package com.advancia.chat4me_messaging_service.Infrastructure.services.impl;

import com.advancia.chat4me_messaging_service.infrastructure.services.impl.SystemDateTimeProviderImpl;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SystemDateTimeProviderImplTest {
    private final SystemDateTimeProviderImpl systemDateTimeProviderImpl = new SystemDateTimeProviderImpl();

    @Test
    void shouldReturnCurrentOffsetDateTime_whenAllOk() {
        OffsetDateTime beforeCall = OffsetDateTime.now();
        OffsetDateTime result = systemDateTimeProviderImpl.now();
        OffsetDateTime afterCall = OffsetDateTime.now();

        assertNotNull(result, "OffsetDateTime should not be null");
        assertTrue(
            !result.isBefore(beforeCall) && !result.isAfter(afterCall),
            "The returned OffsetDateTime must be between before and after the call"
        );
    }
}
