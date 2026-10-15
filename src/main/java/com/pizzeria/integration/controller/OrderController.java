package com.pizzeria.integration.controller;

import com.pizzeria.integration.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pizzeria.integration.model.OrderRequest;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(
            @Valid @RequestBody OrderRequest order) {


          System.out.println(">>> El controlador recibió el pedido");

        String result = orderService.processOrder(order);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(result);
    }
}