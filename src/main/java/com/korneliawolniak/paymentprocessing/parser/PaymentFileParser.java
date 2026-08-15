package com.korneliawolniak.paymentprocessing.parser;

import com.korneliawolniak.paymentprocessing.input.PaymentInput;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class PaymentFileParser {

    private final ObjectMapper objectMapper;

    public PaymentFileParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public PaymentInput parse(InputStream inputStream) throws IOException {
        return objectMapper.readValue(inputStream, PaymentInput.class);
    }
}