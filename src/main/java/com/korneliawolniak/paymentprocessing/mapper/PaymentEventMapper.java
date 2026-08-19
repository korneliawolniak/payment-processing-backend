package com.korneliawolniak.paymentprocessing.mapper;

import com.korneliawolniak.paymentprocessing.avro.PaymentCreatedEvent;
import com.korneliawolniak.paymentprocessing.avro.TransactionEvent;
import com.korneliawolniak.paymentprocessing.domain.Payment;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventMapper {

  public PaymentCreatedEvent toEvent(Payment payment) {
    List<TransactionEvent> transactionEvents =
        payment.transactions().stream()
            .map(
                transaction ->
                    TransactionEvent.newBuilder()
                        .setTransactionId(transaction.id().toString())
                        .setPaymentId(transaction.paymentId().toString())
                        .setCreditorName(transaction.creditor().name())
                        .setCreditorAccountNumber(transaction.creditor().accountNumber())
                        .setAmount(transaction.amount().toPlainString())
                        .build())
            .toList();

    return PaymentCreatedEvent.newBuilder()
        .setPaymentId(payment.id().toString())
        .setDebtorName(payment.debtor().name())
        .setDebtorAccountNumber(payment.debtor().accountNumber())
        .setCurrency(payment.currency())
        .setTransactionCount(payment.transactionCount())
        .setTotalAmount(payment.totalAmount().toPlainString())
        .setTransactions(transactionEvents)
        .build();
  }
}
