package com.alertdesk.customer.repository;

import com.alertdesk.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("""
            select distinct c
            from Customer c
            left join c.accountNumbers accountNumber
            where lower(c.fullName) like lower(concat('%', :query, '%'))
               or accountNumber like concat('%', :query, '%')
            """)
    List<Customer> searchByNameOrAccountNumber(@Param("query") String query);
}
