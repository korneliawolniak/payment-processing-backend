package com.korneliawolniak.paymentprocessing.orchestrator;

import com.korneliawolniak.paymentprocessing.avro.PaymentCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentOrchestrator {

  @KafkaListener(topics = "payment-created", groupId = "payment-orchestrator")
  public void handle(PaymentCreatedEvent event) {
    System.out.println("Received payment: " + event.getPaymentId());
  }
}
