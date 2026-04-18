package com.alertdesk.customer.api;

import com.alertdesk.customer.model.AlertSeverity;

import java.time.LocalDateTime;

public record CustomerAlertResponse(
        Long id,
        String alertType,
        String description,
        AlertSeverity severity,
        LocalDateTime createdAt
) {
}
