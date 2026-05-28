package org.example.payment;

import org.example.external.ExternalStripeAPI;
import org.example.log.ILogger;
import org.example.log.LogType;

public class StripePaymentAdapter implements PaymentProcessor {

    private final ExternalStripeAPI stripeAPI;
    private final ILogger logger;
    private final String defaultCurrency;

    public StripePaymentAdapter(ExternalStripeAPI stripeAPI, ILogger logger, String defaultCurrency) {
        this.stripeAPI = stripeAPI;
        this.logger = logger;
        this.defaultCurrency = defaultCurrency;
    }

    @Override
    public PaymentResult processPayment(String customerId, double amount) {
        logger.log(LogType.SYSTEM, "Adapter: Translating payment request for customer " + customerId);

        int amountInCents = (int) Math.round(amount * 100);
        String stripeToken = "tok_" + customerId;

        String response = stripeAPI.createCharge(stripeToken, amountInCents, defaultCurrency);

        logger.log(LogType.SYSTEM, "Adapter: Received raw response -> " + response);

        if (response != null && response.startsWith("SUCCESS")) {
            String transactionId = response.split(": ")[1];
            return new PaymentResult(true, transactionId, null);
        }

        String errorMessage = response != null && response.contains(": ")
                ? response.split(": ")[1]
                : "Unknown Error";

        return new PaymentResult(false, null, errorMessage);
    }
}