package com.alertdesk.customer.service;

import com.alertdesk.customer.api.CustomerAlertResponse;
import com.alertdesk.customer.api.CustomerProfileResponse;
import com.alertdesk.customer.api.CustomerSearchResponse;
import com.alertdesk.customer.common.exception.GlobalExceptionHandler.BusinessRuleException;
import com.alertdesk.customer.common.exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.alertdesk.customer.model.Customer;
import com.alertdesk.customer.model.CustomerAlert;
import com.alertdesk.customer.repository.CustomerAlertRepository;
import com.alertdesk.customer.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final CustomerAlertRepository customerAlertRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerAlertRepository customerAlertRepository) {
        this.customerRepository = customerRepository;
        this.customerAlertRepository = customerAlertRepository;
    }

    public CustomerProfileResponse getCustomerProfile(String customerId) {
        log.info("Fetching customer profile for customerId={}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.warn("Customer profile not found for customerId={}", customerId);
                    return new ResourceNotFoundException("Customer not found: " + customerId);
                });
        CustomerProfileResponse response = mapProfile(customer);
        log.info("Fetched customer profile for customerId={}", customerId);
        return response;
    }

    public List<CustomerAlertResponse> getCustomerAlerts(String customerId) {
        log.info("Fetching customer alerts for customerId={}", customerId);
        if (!customerRepository.existsById(customerId)) {
            log.warn("Customer alerts requested for unknown customerId={}", customerId);
            throw new ResourceNotFoundException("Customer not found: " + customerId);
        }
        List<CustomerAlertResponse> responses = customerAlertRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::mapAlert)
                .toList();
        log.info("Fetched {} alerts for customerId={}", responses.size(), customerId);
        return responses;
    }

    public List<CustomerSearchResponse> searchCustomers(String query) {
        String normalizedQuery = query == null ? "" : query.trim();
        log.info("Searching customers with query='{}'", normalizedQuery);
        if (normalizedQuery.length() < 3) {
            log.warn("Rejected customer search because query was shorter than 3 characters");
            throw new BusinessRuleException("Search query must be at least 3 characters");
        }
        List<CustomerSearchResponse> responses = customerRepository.searchByNameOrAccountNumber(normalizedQuery)
                .stream()
                .map(this::mapSearch)
                .toList();
        log.info("Customer search returned {} result(s) for query='{}'", responses.size(), normalizedQuery);
        return responses;
    }

    private CustomerProfileResponse mapProfile(Customer customer) {
        return new CustomerProfileResponse(
                customer.getCustomerId(),
                customer.getFullName(),
                customer.getAccountNumbers(),
                customer.getRiskRating(),
                customer.isPepFlag(),
                customer.isSanctionsMatch(),
                customer.getKycStatus(),
                customer.getOnboardedDate(),
                customer.getLastReviewDate()
        );
    }

    private CustomerAlertResponse mapAlert(CustomerAlert alert) {
        return new CustomerAlertResponse(
                alert.getId(),
                alert.getAlertType(),
                alert.getDescription(),
                alert.getSeverity(),
                alert.getCreatedAt()
        );
    }

    private CustomerSearchResponse mapSearch(Customer customer) {
        return new CustomerSearchResponse(
                customer.getCustomerId(),
                customer.getFullName(),
                customer.getAccountNumbers(),
                customer.getRiskRating(),
                customer.getKycStatus()
        );
    }
}
