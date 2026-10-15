package com.pizzeria.integration.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class OrderRequestValidationTest {

    @Test
    void shouldRejectOrderWithEmptyItems() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            OrderRequest order = new OrderRequest(
                    "Enrique",
                    "WHATSAPP",
                    "URGENT",
                    List.of()
            );

            var violations = validator.validate(order);

            violations.forEach(violation ->
                    System.out.println(
                            violation.getPropertyPath()
                                    + ": "
                                    + violation.getMessage()
                    )
            );

            assertFalse(
                    violations.isEmpty(),
                    "La validación debería rechazar un pedido sin productos"
            );
        }
    }
}