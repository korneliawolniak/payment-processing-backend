package com.korneliawolniak.paymentprocessing.orchestrator;

import com.korneliawolniak.paymentprocessing.avro.PaymentCreatedEvent;
import com.korneliawolniak.paymentprocessing.avro.PaymentValidationRequest;
import com.korneliawolniak.paymentprocessing.avro.PaymentValidationResult;
import com.korneliawolniak.paymentprocessing.kafka.PaymentValidationRequestPublisher;
import com.korneliawolniak.paymentprocessing.mapper.PaymentValidationRequestMapper;
import com.korneliawolniak.paymentprocessing.persistence.PaymentEntity;
import com.korneliawolniak.paymentprocessing.persistence.PaymentRepository;
import com.korneliawolniak.paymentprocessing.persistence.PaymentStatus;
import java.util.UUID;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentOrchestrator {

  private final PaymentRepository paymentRepository;
  private final PaymentValidationRequestMapper paymentValidationRequestMapper;
  private final PaymentValidationRequestPublisher paymentValidationRequestPublisher;

  public PaymentOrchestrator(
      PaymentRepository paymentRepository,
      PaymentValidationRequestMapper paymentValidationRequestMapper,
      PaymentValidationRequestPublisher paymentValidationRequestPublisher) {
    this.paymentRepository = paymentRepository;
    this.paymentValidationRequestMapper = paymentValidationRequestMapper;
    this.paymentValidationRequestPublisher = paymentValidationRequestPublisher;
  }

  @KafkaListener(topics = "payment-created", groupId = "payment-orchestrator")
  public void handle(PaymentCreatedEvent event) {
    UUID paymentId = UUID.fromString(event.getPaymentId().toString());

    PaymentEntity paymentEntity = new PaymentEntity(paymentId, PaymentStatus.PENDING);

    paymentRepository.save(paymentEntity);

    PaymentValidationRequest validationRequest = paymentValidationRequestMapper.toEvent(event);

    paymentValidationRequestPublisher.publish(validationRequest);

    System.out.println("Saved payment: " + paymentId + " with status PENDING");
  }

  @KafkaListener(topics = "payment-validation-result", groupId = "payment-orchestrator")
  public void handleValidationResult(PaymentValidationResult result) {
    UUID paymentId = UUID.fromString(result.getPaymentId().toString());

    PaymentEntity payment =
        paymentRepository
            .findById(paymentId)
            .orElseThrow(() -> new IllegalStateException("Payment not found: " + paymentId));

    PaymentStatus status = PaymentStatus.valueOf(result.getStatus().toString());

    payment.setStatus(status);

    paymentRepository.save(payment);

    System.out.println("Updated payment: " + paymentId + " to status " + status);
  }
}
