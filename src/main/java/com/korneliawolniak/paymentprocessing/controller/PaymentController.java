package com.korneliawolniak.paymentprocessing.controller;

import com.korneliawolniak.paymentprocessing.domain.Payment;
import com.korneliawolniak.paymentprocessing.input.PaymentInput;
import com.korneliawolniak.paymentprocessing.parser.PaymentFileParser;
import com.korneliawolniak.paymentprocessing.service.PaymentLoader;
import com.korneliawolniak.paymentprocessing.service.PaymentValidator;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

  private final PaymentFileParser paymentFileParser;
  private final PaymentLoader paymentLoader;
  private final PaymentValidator paymentValidator;

  public PaymentController(
      PaymentFileParser paymentFileParser,
      PaymentLoader paymentLoader,
      PaymentValidator paymentValidator) {
    this.paymentFileParser = paymentFileParser;
    this.paymentLoader = paymentLoader;
    this.paymentValidator = paymentValidator;
  }

  @PostMapping("/upload")
  public ResponseEntity<Payment> upload(@RequestParam("file") MultipartFile file)
      throws IOException {
    PaymentInput paymentInput = paymentFileParser.parse(file.getInputStream());
    Payment payment = paymentLoader.load(paymentInput);

    paymentValidator.validate(payment);

    return ResponseEntity.ok(payment);
  }
}
