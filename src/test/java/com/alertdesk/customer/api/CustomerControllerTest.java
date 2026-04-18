package com.alertdesk.customer.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnCustomerProfile() throws Exception {
        mockMvc.perform(get("/api/customers/CUST-1042"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("CUST-1042"))
                .andExpect(jsonPath("$.riskRating").value("HIGH"))
                .andExpect(jsonPath("$.accountNumbers[0]").value("12345678"));
    }

    @Test
    void shouldReturnCustomerAlerts() throws Exception {
        mockMvc.perform(get("/api/customers/CUST-4455/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].alertType").exists());
    }

    @Test
    void shouldSearchCustomersByNameOrAccount() throws Exception {
        mockMvc.perform(get("/api/customers/search").param("query", "mar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("CUST-1042"));

        mockMvc.perform(get("/api/customers/search").param("query", "223"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("CUST-1120"));
    }

    @Test
    void shouldRejectShortSearchQuery() throws Exception {
        mockMvc.perform(get("/api/customers/search").param("query", "ab"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.violations[0].field").value("query"))
                .andExpect(jsonPath("$.violations[0].message").exists());
    }

    @Test
    void shouldReturnNotFoundInSharedErrorShape() throws Exception {
        mockMvc.perform(get("/api/customers/UNKNOWN"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Customer not found: UNKNOWN"))
                .andExpect(jsonPath("$.violations").isArray());
    }
}
