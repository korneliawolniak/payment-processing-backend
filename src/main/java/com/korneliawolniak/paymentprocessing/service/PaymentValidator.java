package com.korneliawolniak.paymentprocessing.service;

import com.korneliawolniak.paymentprocessing.domain.Payment;
import java.math.BigDecimal;

import com.korneliawolniak.paymentprocessing.exception.PaymentValidationException;
import org.springframework.stereotype.Service;

@Service
public class PaymentValidator {

    public void validate(Payment payment) {
        validateTransactionCount(payment);
        validateTotalAmount(payment);
        validateTransactionAmounts(payment);
    }

    private void validateTransactionCount(Payment payment) {
        int actualCount = payment.transactions().size();

        if (actualCount != payment.transactionCount()) {
            throw new PaymentValidationException("Transaction count does not match declared transactionCount");
        }
    }

    private void validateTotalAmount(Payment payment) {
        BigDecimal calculatedTotal =
                payment.transactions().stream()
                        .map(transaction -> transaction.amount())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (calculatedTotal.compareTo(payment.totalAmount()) != 0) {
            throw new PaymentValidationException(
                    "Total amount does not match sum of transaction amounts");
        }
    }

    private void validateTransactionAmounts(Payment payment) {
        boolean hasInvalidAmount =
                payment.transactions().stream()
                        .anyMatch(transaction -> transaction.amount().compareTo(BigDecimal.ZERO) <= 0);

        if (hasInvalidAmount) {
            throw new PaymentValidationException("Transaction amount must be greater than zero");
        }
    }
}