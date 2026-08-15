package com.korneliawolniak.paymentprocessing.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.korneliawolniak.paymentprocessing.domain.Creditor;
import com.korneliawolniak.paymentprocessing.domain.Debtor;
import com.korneliawolniak.paymentprocessing.domain.Payment;
import com.korneliawolniak.paymentprocessing.domain.Transaction;
import com.korneliawolniak.paymentprocessing.exception.PaymentValidationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PaymentValidatorTest {

    private final PaymentValidator paymentValidator = new PaymentValidator();

    @Test
    void shouldAcceptValidPayment() {
        UUID paymentId = UUID.randomUUID();

        Payment payment =
                new Payment(
                        paymentId,
                        new Debtor("Finance Department", "PL111"),
                        "PLN",
                        2,
                        new BigDecimal("150.00"),
                        List.of(
                                new Transaction(
                                        UUID.randomUUID(),
                                        paymentId,
                                        new Creditor("Company A", "PL222"),
                                        new BigDecimal("100.00")),
                                new Transaction(
                                        UUID.randomUUID(),
                                        paymentId,
                                        new Creditor("Company B", "PL333"),
                                        new BigDecimal("50.00"))));

        assertDoesNotThrow(() -> paymentValidator.validate(payment));
    }

    @Test
    void shouldRejectPaymentWhenTransactionCountDoesNotMatch() {
        UUID paymentId = UUID.randomUUID();

        Payment payment =
                new Payment(
                        paymentId,
                        new Debtor("Finance Department", "PL111"),
                        "PLN",
                        3,
                        new BigDecimal("150.00"),
                        List.of(
                                new Transaction(
                                        UUID.randomUUID(),
                                        paymentId,
                                        new Creditor("Company A", "PL222"),
                                        new BigDecimal("100.00")),
                                new Transaction(
                                        UUID.randomUUID(),
                                        paymentId,
                                        new Creditor("Company B", "PL333"),
                                        new BigDecimal("50.00"))));

        assertThrows(
                PaymentValidationException.class,
                () -> paymentValidator.validate(payment));
    }
    @Test
    void shouldRejectPaymentWhenTotalAmountDoesNotMatch() {
        UUID paymentId = UUID.randomUUID();

        Payment payment =
                new Payment(
                        paymentId,
                        new Debtor("Finance Department", "PL111"),
                        "PLN",
                        2,
                        new BigDecimal("140.00"),
                        List.of(
                                new Transaction(
                                        UUID.randomUUID(),
                                        paymentId,
                                        new Creditor("Company A", "PL222"),
                                        new BigDecimal("100.00")),
                                new Transaction(
                                        UUID.randomUUID(),
                                        paymentId,
                                        new Creditor("Company B", "PL333"),
                                        new BigDecimal("50.00"))));

        assertThrows(
                PaymentValidationException.class,
                () -> paymentValidator.validate(payment));
    }
    @Test
    void shouldRejectPaymentWhenTransactionAmountIsNotPositive() {
        UUID paymentId = UUID.randomUUID();

        Payment payment =
                new Payment(
                        paymentId,
                        new Debtor("Finance Department", "PL111"),
                        "PLN",
                        1,
                        BigDecimal.ZERO,
                        List.of(
                                new Transaction(
                                        UUID.randomUUID(),
                                        paymentId,
                                        new Creditor("Company A", "PL222"),
                                        BigDecimal.ZERO)));

        assertThrows(
                PaymentValidationException.class,
                () -> paymentValidator.validate(payment));
    }
}