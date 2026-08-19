package com.korneliawolniak.paymentprocessing.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity
public class PaymentEntity {

  @Id private UUID id;

  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  protected PaymentEntity() {}

  public PaymentEntity(UUID id, PaymentStatus status) {
    this.id = id;
    this.status = status;
  }

  public UUID getId() {
    return id;
  }

  public PaymentStatus getStatus() {
    return status;
  }
}
