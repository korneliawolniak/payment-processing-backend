package com.korneliawolniak.paymentprocessing.input;

import java.math.BigDecimal;

public record TransactionInput(
        CreditorInput creditor,
        BigDecimal amount
) {
}