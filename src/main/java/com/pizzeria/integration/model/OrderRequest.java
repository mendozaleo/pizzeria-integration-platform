package com.pizzeria.integration.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderRequest(

        @NotBlank(message = "El nombre del cliente es obligatorio")
        String customerName,

        @NotBlank(message = "El canal es obligatorio")
        String channel,

        @NotBlank(message = "El tipo de pedido es obligatorio")
        String orderType,

        @NotEmpty(message = "El pedido debe contener al menos un producto")
        List<@Valid @NotNull OrderItem> items

) {

    public record OrderItem(

            @NotBlank(message = "El nombre del producto es obligatorio")
            String product,

            @Positive(message = "La cantidad debe ser mayor que cero")
            int quantity,

            @NotNull(message = "El precio es obligatorio")
            @PositiveOrZero(message = "El precio no puede ser negativo")
            BigDecimal unitPrice

    ) {
    }
}