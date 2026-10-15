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

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        OrderRequest whatsappOrder = createOrder("WHATSAPP", "URGENT");
        OrderRequest webOrder = createOrder("WEB", "NORMAL");

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

    @Test
    void shouldProcessUrgentOrder() {
        OrderRequest order = createOrder("WHATSAPP", "URGENT");

        String result = producerTemplate.requestBody(
                "direct:procesar-pedido",
                order,
                String.class
        );

        assertEquals(
                "Pedido URGENTE recibido para Enrique",
                result
        );
    }

    @Test
    void shouldProcessNormalOrder() {
        OrderRequest order = createOrder("WEB", "NORMAL");

        String result = producerTemplate.requestBody(
                "direct:procesar-pedido",
                order,
                String.class
        );

        assertEquals(
                "Pedido NORMAL recibido para Enrique",
                result
        );
    }

    @Test
    void shouldRejectUnknownOrderType() {
        OrderRequest order = createOrder("WEB", "EXPRESS");

        String result = producerTemplate.requestBody(
                "direct:procesar-pedido",
                order,
                String.class
        );

        assertEquals(
                "Error: tipo de pedido no reconocido",
                result
        );
    }

    private OrderRequest createOrder(String channel, String orderType) {
        return new OrderRequest(
                "Enrique",
                channel,
                orderType,
                List.of(
                        new OrderRequest.OrderItem(
                                "Pizza grande",
                                1,
                                new BigDecimal("190.00")
                        )
                )
        );
    }
}