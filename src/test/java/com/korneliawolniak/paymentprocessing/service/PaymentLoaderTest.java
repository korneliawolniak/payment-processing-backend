package com.korneliawolniak.paymentprocessing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.korneliawolniak.paymentprocessing.domain.Payment;
import com.korneliawolniak.paymentprocessing.input.CreditorInput;
import com.korneliawolniak.paymentprocessing.input.DebtorInput;
import com.korneliawolniak.paymentprocessing.input.PaymentInput;
import com.korneliawolniak.paymentprocessing.input.TransactionInput;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class PaymentLoaderTest {

    private final PaymentLoader paymentLoader = new PaymentLoader();

    @Test
    void shouldLoadPaymentAndAssignIds() {
        PaymentInput input =
                new PaymentInput(
                        new DebtorInput("Finance Department", "PL111"),
                        "PLN",
                        2,
                        new BigDecimal("150.00"),
                        List.of(
                                new TransactionInput(
                                        new CreditorInput("Company A", "PL222"),
                                        new BigDecimal("100.00")),
                                new TransactionInput(
                                        new CreditorInput("Company B", "PL333"),
                                        new BigDecimal("50.00"))));

        Payment payment = paymentLoader.load(input);

        assertNotNull(payment.id());
        assertEquals(2, payment.transactions().size());

        payment.transactions().forEach(transaction -> {
            assertNotNull(transaction.id());
            assertEquals(payment.id(), transaction.paymentId());
        });
    }
}