package com.balindam.order_service.domain;

import com.balindam.order_service.ApplicationProperties;
import com.balindam.order_service.domain.models.OrderCancelledEvent;
import com.balindam.order_service.domain.models.OrderCreatedEvent;
import com.balindam.order_service.domain.models.OrderDeliveredEvent;
import com.balindam.order_service.domain.models.OrderErrorEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties applicationProperties;

    OrderEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties applicationProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.applicationProperties = applicationProperties;
    }

    public void publish(OrderCreatedEvent orderCreatedEvent) {
        this.send(applicationProperties.newOrdersQueue(), orderCreatedEvent);
    }

    public void publish(OrderDeliveredEvent orderDeliveredEvent) {
        this.send(applicationProperties.deliveredOrdersQueue(), orderDeliveredEvent);
    }

    public void publish(OrderCancelledEvent orderCancelledEvent) {
        this.send(applicationProperties.cancelledOrdersQueue(), orderCancelledEvent);
    }

    public void publish(OrderErrorEvent orderErrorEvent) {
        this.send(applicationProperties.errorOrdersQueue(), orderErrorEvent);
    }

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(applicationProperties.orderEventsExchange(), routingKey, payload);
    }
}
