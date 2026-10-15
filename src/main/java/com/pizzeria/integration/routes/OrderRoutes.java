package com.pizzeria.integration.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import com.pizzeria.integration.model.OrderRequest;

@Component
public class OrderRoutes extends RouteBuilder {

    @Override
    public void configure() {

        from("timer:pedido?repeatCount=1")
                .routeId("simular-recepcion-pedido")
                .setBody(constant("Pedido de prueba: pizza grande"))
                .log("Pedido de prueba: ${body}");

        from("direct:procesar-pedido")
                .routeId("procesar-pedido")
                .log("Pedido recibido: ${body}")
                .choice()
                .when(simple("${body.orderType} == 'URGENT'"))
                .log("Enrutando pedido URGENTE")
                .process(exchange -> {
                    OrderRequest order = exchange.getMessage()
                            .getBody(OrderRequest.class);

                    exchange.getMessage().setBody(
                            "Pedido URGENTE recibido para "
                                    + order.customerName());
                })
                .when(simple("${body.orderType} == 'NORMAL'"))
                .log("Enrutando pedido NORMAL")
                .process(exchange -> {
                    OrderRequest order = exchange.getMessage()
                            .getBody(OrderRequest.class);

                    exchange.getMessage().setBody(
                            "Pedido NORMAL recibido para "
                                    + order.customerName());
                })
                .otherwise()
                .log("Tipo de pedido no reconocido")
                .setBody(
                        constant("Error: tipo de pedido no reconocido"))
                .end()
                .log("Resultado: ${body}");

        from("direct:filtrar-pedidos-whatsapp")
                .routeId("filtrar-pedidos-whatsapp")
                .filter(exchange -> {
                    OrderRequest order = exchange.getMessage()
                            .getBody(OrderRequest.class);

                    return "WHATSAPP".equals(order.channel());
                })
                .log("Pedido aceptado desde WhatsApp: ${body}")
                .to("mock:pedido-whatsapp")
                .end();
    }
}
