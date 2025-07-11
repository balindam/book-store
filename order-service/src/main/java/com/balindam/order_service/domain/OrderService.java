package com.balindam.order_service.domain;

import com.balindam.order_service.domain.models.CreateOrderRequest;
import com.balindam.order_service.domain.models.CreateOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest createOrderRequest) {
        orderValidator.validate(createOrderRequest);
        OrderEntity newOrder = OrderMapper.convertToEntity(createOrderRequest);
        newOrder.setUserName(userName);
        OrderEntity savedOrder = orderRepository.save(newOrder);
        log.info("Created Order with order number: {}", savedOrder.getOrderNumber());
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }
}
