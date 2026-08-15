package com.korneliawolniak.paymentprocessing.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record Transaction(
        UUID id,
        UUID paymentId,
        Creditor creditor,
        BigDecimal amount
) {
}