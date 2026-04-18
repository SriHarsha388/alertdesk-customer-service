package com.alertdesk.customer.repository;

import com.alertdesk.customer.model.CustomerAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerAlertRepository extends JpaRepository<CustomerAlert, Long> {

    List<CustomerAlert> findByCustomerCustomerIdOrderByCreatedAtDesc(String customerId);
}
