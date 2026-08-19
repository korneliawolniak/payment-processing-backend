package com.korneliawolniak.paymentprocessing.orchestrator;

import com.korneliawolniak.paymentprocessing.avro.PaymentCreatedEvent;
import com.korneliawolniak.paymentprocessing.persistence.PaymentEntity;
import com.korneliawolniak.paymentprocessing.persistence.PaymentRepository;
import com.korneliawolniak.paymentprocessing.persistence.PaymentStatus;
import java.util.UUID;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentOrchestrator {

  private final PaymentRepository paymentRepository;

  public PaymentOrchestrator(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }

  @KafkaListener(topics = "payment-created", groupId = "payment-orchestrator")
  public void handle(PaymentCreatedEvent event) {
    UUID paymentId = UUID.fromString(event.getPaymentId().toString());

    PaymentEntity paymentEntity = new PaymentEntity(paymentId, PaymentStatus.PENDING);

    paymentRepository.save(paymentEntity);

    System.out.println("Saved payment: " + paymentId + " with status PENDING");
  }
}
