package org.example.payment;

public interface PaymentProcessor {
    PaymentResult processPayment(String customerId, double amount);
}
