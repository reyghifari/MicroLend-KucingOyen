package com.kucingoyen.microlend.exception;

/**
 * Exception thrown when a user has insufficient balance for a transaction.
 */
public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
