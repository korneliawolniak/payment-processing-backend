package com.korneliawolniak.paymentprocessing.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record Payment(
        UUID id,
        Debtor debtor,
        String currency,
        int transactionCount,
        BigDecimal totalAmount,
        List<Transaction> transactions
) {
}