package org.example.external;

public class ExternalStripeAPI {

    public String createCharge(String token, int amountInCents, String currency) {
        if (amountInCents <= 0) {
            return "ERROR: Invalid Amount";
        }
        if (token == null || token.isEmpty()) {
            return "ERROR: Missing Token";
        }

        return "SUCCESS: ch_" + System.currentTimeMillis();
    }
}
