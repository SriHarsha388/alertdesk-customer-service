package com.alertdesk.customer.service;

import com.alertdesk.customer.api.CustomerAlertResponse;
import com.alertdesk.customer.api.CustomerProfileResponse;
import com.alertdesk.customer.api.CustomerSearchResponse;
import com.alertdesk.customer.common.exception.GlobalExceptionHandler.BusinessRuleException;
import com.alertdesk.customer.common.exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.alertdesk.customer.model.AlertSeverity;
import com.alertdesk.customer.model.Customer;
import com.alertdesk.customer.model.CustomerAlert;
import com.alertdesk.customer.model.KycStatus;
import com.alertdesk.customer.model.RiskRating;
import com.alertdesk.customer.repository.CustomerAlertRepository;
import com.alertdesk.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerAlertRepository customerAlertRepository;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository, customerAlertRepository);
    }

    @Test
    void shouldReturnCustomerProfile() {
        Customer customer = sampleCustomer();
        when(customerRepository.findById("CUST-1042")).thenReturn(Optional.of(customer));

        CustomerProfileResponse response = customerService.getCustomerProfile("CUST-1042");

        assertThat(response.customerId()).isEqualTo("CUST-1042");
        assertThat(response.fullName()).isEqualTo("Marcus T. Oyelaran");
        assertThat(response.accountNumbers()).containsExactly("12345678", "87654321");
        assertThat(response.riskRating()).isEqualTo(RiskRating.HIGH);
    }

    @Test
    void shouldThrowNotFoundWhenCustomerProfileDoesNotExist() {
        when(customerRepository.findById("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerProfile("UNKNOWN"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer not found: UNKNOWN");
    }

    @Test
    void shouldReturnCustomerAlertsInRepositoryOrder() {
        Customer customer = sampleCustomer();
        CustomerAlert latest = new CustomerAlert(
                customer,
                "PEP_REVIEW",
                "Enhanced due diligence review created.",
                AlertSeverity.HIGH,
                LocalDateTime.parse("2024-01-26T08:20:00")
        );
        CustomerAlert older = new CustomerAlert(
                customer,
                "PROFILE_REFRESH",
                "Periodic customer review completed.",
                AlertSeverity.LOW,
                LocalDateTime.parse("2023-09-01T09:00:00")
        );

        when(customerRepository.existsById("CUST-1042")).thenReturn(true);
        when(customerAlertRepository.findByCustomerCustomerIdOrderByCreatedAtDesc("CUST-1042"))
                .thenReturn(List.of(latest, older));

        List<CustomerAlertResponse> response = customerService.getCustomerAlerts("CUST-1042");

        assertThat(response).hasSize(2);
        assertThat(response.get(0).alertType()).isEqualTo("PEP_REVIEW");
        assertThat(response.get(1).alertType()).isEqualTo("PROFILE_REFRESH");
    }

    @Test
    void shouldThrowNotFoundWhenAlertsRequestedForUnknownCustomer() {
        when(customerRepository.existsById("UNKNOWN")).thenReturn(false);

        assertThatThrownBy(() -> customerService.getCustomerAlerts("UNKNOWN"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer not found: UNKNOWN");

        verifyNoInteractions(customerAlertRepository);
    }

    @Test
    void shouldSearchCustomersByNameOrAccountNumber() {
        Customer customer = sampleCustomer();
        when(customerRepository.searchByNameOrAccountNumber("mar")).thenReturn(List.of(customer));

        List<CustomerSearchResponse> response = customerService.searchCustomers("mar");

        assertThat(response).singleElement().satisfies(item -> {
            assertThat(item.customerId()).isEqualTo("CUST-1042");
            assertThat(item.fullName()).isEqualTo("Marcus T. Oyelaran");
            assertThat(item.accountNumbers()).containsExactly("12345678", "87654321");
            assertThat(item.kycStatus()).isEqualTo(KycStatus.VERIFIED);
        });
    }

    @Test
    void shouldTrimSearchQueryBeforeSearching() {
        when(customerRepository.searchByNameOrAccountNumber("1042")).thenReturn(List.of(sampleCustomer()));

        List<CustomerSearchResponse> response = customerService.searchCustomers(" 1042 ");

        assertThat(response).hasSize(1);
        verify(customerRepository).searchByNameOrAccountNumber("1042");
    }

    @Test
    void shouldThrowBusinessRuleExceptionWhenSearchQueryIsTooShort() {
        assertThatThrownBy(() -> customerService.searchCustomers("ab"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Search query must be at least 3 characters");

        verifyNoInteractions(customerRepository);
    }

    @Test
    void shouldThrowBusinessRuleExceptionWhenSearchQueryIsBlankAfterTrim() {
        assertThatThrownBy(() -> customerService.searchCustomers("  "))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Search query must be at least 3 characters");

        verifyNoInteractions(customerRepository);
    }

    @Test
    void shouldReturnEmptyListWhenSearchHasNoMatches() {
        when(customerRepository.searchByNameOrAccountNumber("xyz")).thenReturn(List.of());

        List<CustomerSearchResponse> response = customerService.searchCustomers("xyz");

        assertThat(response).isEmpty();
    }

    private Customer sampleCustomer() {
        return new Customer(
                "CUST-1042",
                "Marcus T. Oyelaran",
                List.of("12345678", "87654321"),
                RiskRating.HIGH,
                false,
                false,
                KycStatus.VERIFIED,
                LocalDate.parse("2019-03-14"),
                LocalDate.parse("2023-09-01")
        );
    }
}
