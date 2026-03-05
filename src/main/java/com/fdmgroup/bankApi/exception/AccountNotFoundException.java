package com.fdmgroup.bankApi.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

// Custom exception thrown when an Account cannot be found in the system.
// The @ResponseStatus annotation tells Spring to automatically return an HTTP 404 (Not Found)
// response whenever this exception is thrown.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {

    // Creates a new AccountNotFoundException with a custom error message.
    public AccountNotFoundException(String message) {
        // Call the RuntimeException constructor with the provided message.
        super(message);
    }
}