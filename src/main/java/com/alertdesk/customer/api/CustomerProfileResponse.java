package com.alertdesk.customer.api;

import com.alertdesk.customer.model.KycStatus;
import com.alertdesk.customer.model.RiskRating;

import java.time.LocalDate;
import java.util.List;

public record CustomerProfileResponse(
        String customerId,
        String fullName,
        List<String> accountNumbers,
        RiskRating riskRating,
        boolean pepFlag,
        boolean sanctionsMatch,
        KycStatus kycStatus,
        LocalDate onboardedDate,
        LocalDate lastReviewDate
) {
}
