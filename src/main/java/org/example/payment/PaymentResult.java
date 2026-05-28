package org.example.payment;

public record PaymentResult(boolean success, String transactionId, String errorMessage) {
}