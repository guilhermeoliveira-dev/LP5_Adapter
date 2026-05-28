package org.example.payment;

import org.example.external.ExternalStripeAPI;
import org.example.log.ILogger;
import org.example.log.MockLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentAdapterTest {

    private MockLogger spyLogger;
    private ExternalStripeAPI stripeAPI;
    private PaymentProcessor adapter;
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        spyLogger = MockLogger.get();
        stripeAPI = new ExternalStripeAPI();
        adapter = new StripePaymentAdapter(stripeAPI, spyLogger, "BRL");
        checkoutService = new CheckoutService(adapter, spyLogger);
    }

    @Test
    void givenValidAmount_whenProcessPayment_thenReturnsSuccessResultWithTransactionId() {
        PaymentResult result = adapter.processPayment("CUST-999", 150.50);

        assertTrue(result.success());
        assertNotNull(result.transactionId());
        assertTrue(result.transactionId().startsWith("ch_"));
        assertNull(result.errorMessage());
    }

    @Test
    void givenInvalidAmount_whenProcessPayment_thenReturnsFailureResultWithErrorMessage() {
        PaymentResult result = adapter.processPayment("CUST-999", -10.00);

        assertFalse(result.success());
        assertNull(result.transactionId());
        assertEquals("Invalid Amount", result.errorMessage());
    }

    @Test
    void givenValidCheckout_whenCheckoutRuns_thenLogsTransactionId() {
        checkoutService.checkout("CART-001", "CUST-123", 99.99);

        assertTrue(spyLogger.containsLog("CheckoutService: Order confirmed. TX: ch_"));
    }

    @Test
    void givenInvalidCheckout_whenCheckoutRuns_thenLogsFailureReason() {
        checkoutService.checkout("CART-002", "CUST-456", -50.0);

        assertTrue(spyLogger.containsLog("CheckoutService: Payment failed. Reason: Invalid Amount"));
    }
}