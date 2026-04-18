package com.alertdesk.customer.service;

public class InvalidSearchQueryException extends RuntimeException {

    public InvalidSearchQueryException() {
        super("Search query must be at least 3 characters");
    }
}
