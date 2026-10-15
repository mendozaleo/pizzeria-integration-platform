package com.pizzeria.integration.service;

import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Service;
import com.pizzeria.integration.model.OrderRequest;

@Service
public class OrderService {

    private final ProducerTemplate producerTemplate;

    public OrderService(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public String processOrder(OrderRequest order) {
        return producerTemplate.requestBody(
                "direct:procesar-pedido",
                order,
                String.class
        );
    }
}