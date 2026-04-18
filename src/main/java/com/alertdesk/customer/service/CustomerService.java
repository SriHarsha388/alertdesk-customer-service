package com.alertdesk.customer.service;

import com.alertdesk.customer.api.CustomerAlertResponse;
import com.alertdesk.customer.api.CustomerProfileResponse;
import com.alertdesk.customer.api.CustomerSearchResponse;
import com.alertdesk.customer.model.Customer;
import com.alertdesk.customer.model.CustomerAlert;
import com.alertdesk.customer.repository.CustomerAlertRepository;
import com.alertdesk.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerAlertRepository customerAlertRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerAlertRepository customerAlertRepository) {
        this.customerRepository = customerRepository;
        this.customerAlertRepository = customerAlertRepository;
    }

    public CustomerProfileResponse getCustomerProfile(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        return mapProfile(customer);
    }

    public List<CustomerAlertResponse> getCustomerAlerts(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
        return customerAlertRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::mapAlert)
                .toList();
    }

    public List<CustomerSearchResponse> searchCustomers(String query) {
        String normalizedQuery = query == null ? "" : query.trim();
        if (normalizedQuery.length() < 3) {
            throw new InvalidSearchQueryException();
        }
        return customerRepository.searchByNameOrAccountNumber(normalizedQuery)
                .stream()
                .map(this::mapSearch)
                .toList();
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
