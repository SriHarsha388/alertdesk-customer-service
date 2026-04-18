package com.alertdesk.customer.api;

import com.alertdesk.customer.model.KycStatus;
import com.alertdesk.customer.model.RiskRating;

import java.util.List;

public record CustomerSearchResponse(
        String customerId,
        String fullName,
        List<String> accountNumbers,
        RiskRating riskRating,
        KycStatus kycStatus
) {
}
