package com.korneliawolniak.paymentprocessing.service;

import com.korneliawolniak.paymentprocessing.domain.Creditor;
import com.korneliawolniak.paymentprocessing.domain.Debtor;
import com.korneliawolniak.paymentprocessing.domain.Payment;
import com.korneliawolniak.paymentprocessing.domain.Transaction;
import com.korneliawolniak.paymentprocessing.input.PaymentInput;
import com.korneliawolniak.paymentprocessing.input.TransactionInput;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentLoader {

    public Payment load(PaymentInput paymentInput) {
        UUID paymentId = UUID.randomUUID();

        Debtor debtor =
                new Debtor(
                        paymentInput.debtor().name(),
                        paymentInput.debtor().accountNumber());

        List<Transaction> transactions =
                paymentInput.transactions().stream()
                        .map(transactionInput -> mapTransaction(transactionInput, paymentId))
                        .toList();

        return new Payment(
                paymentId,
                debtor,
                paymentInput.currency(),
                paymentInput.transactionCount(),
                paymentInput.totalAmount(),
                transactions);
    }

    private Transaction mapTransaction(TransactionInput transactionInput, UUID paymentId) {
        Creditor creditor =
                new Creditor(
                        transactionInput.creditor().name(),
                        transactionInput.creditor().accountNumber());

        return new Transaction(
                UUID.randomUUID(),
                paymentId,
                creditor,
                transactionInput.amount());
    }
}