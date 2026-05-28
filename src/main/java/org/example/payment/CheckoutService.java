package org.example.payment;

import org.example.log.ILogger;
import org.example.log.LogType;

public class CheckoutService {

    private final PaymentProcessor paymentProcessor;
    private final ILogger logger;

    public CheckoutService(PaymentProcessor paymentProcessor, ILogger logger) {
        this.paymentProcessor = paymentProcessor;
        this.logger = logger;
    }

    public void checkout(String cartId, String customerId, double totalAmount) {
        logger.log(LogType.SYSTEM, "CheckoutService: Starting checkout for cart " + cartId);

        PaymentResult result = paymentProcessor.processPayment(customerId, totalAmount);

        if (result.success()) {
            logger.log(LogType.SYSTEM, "CheckoutService: Order confirmed. TX: " + result.transactionId());
        } else {
            logger.log(LogType.EXCEPTION, "CheckoutService: Payment failed. Reason: " + result.errorMessage());
        }
    }
}