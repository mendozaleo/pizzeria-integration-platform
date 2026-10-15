package com.pizzeria.integration.routes;

import com.pizzeria.integration.model.OrderRequest;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class OrderRoutesTest {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CamelContext camelContext;

    @Test
    void shouldAcceptOnlyWhatsAppOrders() throws Exception {
        MockEndpoint mockEndpoint = camelContext.getEndpoint(
                "mock:pedido-whatsapp",
                MockEndpoint.class
        );

        mockEndpoint.reset();
        mockEndpoint.expectedMessageCount(1);

        OrderRequest whatsappOrder = new OrderRequest(
                "Enrique",
                "WHATSAPP",
                "URGENT",
                List.of(new OrderRequest.OrderItem(
                        "Pizza grande",
                        1,
                        new BigDecimal("190.00")
                ))
        );

        OrderRequest webOrder = new OrderRequest(
                "Enrique",
                "WEB",
                "NORMAL",
                List.of(new OrderRequest.OrderItem(
                        "Pizza mediana",
                        1,
                        new BigDecimal("120.00")
                ))
        );

        producerTemplate.sendBody(
                "direct:filtrar-pedidos-whatsapp",
                whatsappOrder
        );

        producerTemplate.sendBody(
                "direct:filtrar-pedidos-whatsapp",
                webOrder
        );

        mockEndpoint.assertIsSatisfied();
    }
}
