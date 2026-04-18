package com.alertdesk.customer.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id", nullable = false, updatable = false)
    private String customerId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "customer_accounts", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "account_number", nullable = false)
    private List<String> accountNumbers = new ArrayList<>();

    @Column(name = "risk_rating", nullable = false)
    private RiskRating riskRating;

    @Column(name = "pep_flag", nullable = false)
    private boolean pepFlag;

    @Column(name = "sanctions_match", nullable = false)
    private boolean sanctionsMatch;

    @Column(name = "kyc_status", nullable = false)
    private KycStatus kycStatus;

    @Column(name = "onboarded_date", nullable = false)
    private LocalDate onboardedDate;

    @Column(name = "last_review_date", nullable = false)
    private LocalDate lastReviewDate;

    protected Customer() {
    }

    public Customer(String customerId,
                    String fullName,
                    List<String> accountNumbers,
                    RiskRating riskRating,
                    boolean pepFlag,
                    boolean sanctionsMatch,
                    KycStatus kycStatus,
                    LocalDate onboardedDate,
                    LocalDate lastReviewDate) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.accountNumbers = new ArrayList<>(accountNumbers);
        this.riskRating = riskRating;
        this.pepFlag = pepFlag;
        this.sanctionsMatch = sanctionsMatch;
        this.kycStatus = kycStatus;
        this.onboardedDate = onboardedDate;
        this.lastReviewDate = lastReviewDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public List<String> getAccountNumbers() {
        return accountNumbers;
    }

    public RiskRating getRiskRating() {
        return riskRating;
    }

    public boolean isPepFlag() {
        return pepFlag;
    }

    public boolean isSanctionsMatch() {
        return sanctionsMatch;
    }

    public KycStatus getKycStatus() {
        return kycStatus;
    }

    public LocalDate getOnboardedDate() {
        return onboardedDate;
    }

    public LocalDate getLastReviewDate() {
        return lastReviewDate;
    }
}
