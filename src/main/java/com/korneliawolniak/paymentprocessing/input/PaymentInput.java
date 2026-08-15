package com.korneliawolniak.paymentprocessing.input;

import java.math.BigDecimal;
import java.util.List;

public record PaymentInput(
        DebtorInput debtor,
        String currency,
        int transactionCount,
        BigDecimal totalAmount,
        List<TransactionInput> transactions
) {
}