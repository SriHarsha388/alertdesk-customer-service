package com.alertdesk.customer.api;

import com.alertdesk.customer.service.CustomerService;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public CustomerProfileResponse getCustomer(@PathVariable("id") String customerId) {
        return customerService.getCustomerProfile(customerId);
    }

    @GetMapping("/{id}/alerts")
    public List<CustomerAlertResponse> getCustomerAlerts(@PathVariable("id") String customerId) {
        return customerService.getCustomerAlerts(customerId);
    }

    @GetMapping("/search")
    public List<CustomerSearchResponse> searchCustomers(@RequestParam("query") @Size(min = 3) String query) {
        return customerService.searchCustomers(query);
    }
}
