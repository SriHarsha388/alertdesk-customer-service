package com.alertdesk.customer.config;

import com.alertdesk.customer.model.AlertSeverity;
import com.alertdesk.customer.model.Customer;
import com.alertdesk.customer.model.CustomerAlert;
import com.alertdesk.customer.model.KycStatus;
import com.alertdesk.customer.model.RiskRating;
import com.alertdesk.customer.repository.CustomerAlertRepository;
import com.alertdesk.customer.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(CustomerRepository customerRepository, CustomerAlertRepository customerAlertRepository) {
        return args -> {
            if (customerRepository.count() > 0) {
                return;
            }

            Customer customer1042 = new Customer("CUST-1042", "Marcus T. Oyelaran", List.of("12345678", "87654321"), RiskRating.HIGH, false, false, KycStatus.VERIFIED, LocalDate.parse("2019-03-14"), LocalDate.parse("2023-09-01"));
            Customer customer2187 = new Customer("CUST-2187", "Diane K. Ashworth", List.of("23456789"), RiskRating.HIGH, false, false, KycStatus.UNDER_REVIEW, LocalDate.parse("2021-07-22"), LocalDate.parse("2024-01-15"));
            Customer customer3301 = new Customer("CUST-3301", "Patrick J. Nkemdirim", List.of("34567890"), RiskRating.MEDIUM, false, false, KycStatus.VERIFIED, LocalDate.parse("2020-11-05"), LocalDate.parse("2023-11-05"));
            Customer customer4455 = new Customer("CUST-4455", "Valentina R. Kozlova", List.of("45678901", "56789012"), RiskRating.HIGH, true, true, KycStatus.UNDER_REVIEW, LocalDate.parse("2018-06-30"), LocalDate.parse("2024-01-26"));
            Customer customer5509 = new Customer("CUST-5509", "Ahmed S. Al-Rashidi", List.of("67890123"), RiskRating.MEDIUM, false, false, KycStatus.VERIFIED, LocalDate.parse("2022-02-18"), LocalDate.parse("2023-02-18"));
            Customer customer6612 = new Customer("CUST-6612", "Sandra L. Whitmore", List.of("78901234"), RiskRating.MEDIUM, false, false, KycStatus.VERIFIED, LocalDate.parse("2017-09-12"), LocalDate.parse("2023-09-12"));
            Customer customer7734 = new Customer("CUST-7734", "Oluwaseun B. Adeyemi", List.of("89012345"), RiskRating.LOW, false, false, KycStatus.VERIFIED, LocalDate.parse("2016-04-22"), LocalDate.parse("2023-04-22"));
            Customer customer8821 = new Customer("CUST-8821", "Claire M. Buchanan", List.of("90123456"), RiskRating.LOW, false, false, KycStatus.VERIFIED, LocalDate.parse("2020-08-09"), LocalDate.parse("2023-08-09"));
            Customer customer9043 = new Customer("CUST-9043", "Dmitri V. Sorokin", List.of("01234567", "11223344"), RiskRating.HIGH, true, false, KycStatus.UNDER_REVIEW, LocalDate.parse("2015-12-03"), LocalDate.parse("2024-02-10"));
            Customer customer1120 = new Customer("CUST-1120", "Fatima N. Al-Hassan", List.of("22334455"), RiskRating.LOW, false, false, KycStatus.VERIFIED, LocalDate.parse("2021-03-17"), LocalDate.parse("2023-03-17"));

            List<Customer> customers = List.of(
                    customer1042, customer2187, customer3301, customer4455, customer5509,
                    customer6612, customer7734, customer8821, customer9043, customer1120
            );
            customerRepository.saveAll(customers);

            customerAlertRepository.saveAll(List.of(
                    new CustomerAlert(customer1042, "LARGE_CASH_MOVEMENT", "Multiple same-day cash deposits exceeded the review threshold.", AlertSeverity.HIGH, LocalDateTime.parse("2024-02-05T10:15:00")),
                    new CustomerAlert(customer1042, "PROFILE_REFRESH", "Periodic customer review completed with no further action required.", AlertSeverity.LOW, LocalDateTime.parse("2023-09-01T09:00:00")),
                    new CustomerAlert(customer2187, "KYC_DOCUMENT_GAP", "Proof of address expired and updated documentation is pending.", AlertSeverity.MEDIUM, LocalDateTime.parse("2024-01-15T14:30:00")),
                    new CustomerAlert(customer3301, "PAYMENT_PATTERN_CHANGE", "International transfer pattern changed materially over the last 30 days.", AlertSeverity.MEDIUM, LocalDateTime.parse("2024-03-12T11:45:00")),
                    new CustomerAlert(customer4455, "SANCTIONS_SCREENING", "Potential sanctions hit requires analyst escalation.", AlertSeverity.HIGH, LocalDateTime.parse("2024-01-26T08:20:00")),
                    new CustomerAlert(customer4455, "PEP_REVIEW", "Enhanced due diligence review created due to PEP classification.", AlertSeverity.HIGH, LocalDateTime.parse("2023-12-04T16:10:00")),
                    new CustomerAlert(customer5509, "SOURCE_OF_FUNDS_CHECK", "Source of funds evidence was sampled and validated.", AlertSeverity.LOW, LocalDateTime.parse("2023-02-18T10:00:00")),
                    new CustomerAlert(customer6612, "ADDRESS_CONFIRMATION", "Annual customer profile check completed successfully.", AlertSeverity.LOW, LocalDateTime.parse("2023-09-12T09:30:00")),
                    new CustomerAlert(customer7734, "LOW_RISK_MONITORING", "Routine low-risk monitoring cycle completed.", AlertSeverity.LOW, LocalDateTime.parse("2023-04-22T13:00:00")),
                    new CustomerAlert(customer8821, "ACCOUNT_ACTIVITY_REVIEW", "Dormant account became active and was reviewed.", AlertSeverity.MEDIUM, LocalDateTime.parse("2023-10-08T12:25:00")),
                    new CustomerAlert(customer9043, "PEP_REVIEW", "PEP recertification review opened after adverse media screening.", AlertSeverity.HIGH, LocalDateTime.parse("2024-02-10T17:05:00")),
                    new CustomerAlert(customer1120, "STANDARD_REVIEW", "Annual customer due diligence cycle completed.", AlertSeverity.LOW, LocalDateTime.parse("2023-03-17T08:45:00"))
            ));
        };
    }
}
