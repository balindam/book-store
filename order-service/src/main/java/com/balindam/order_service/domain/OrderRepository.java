package com.balindam.order_service.domain;

import com.balindam.order_service.domain.models.OrderSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("""
            SELECT com.balindam.order_service.domain.models.OrderSummary(o.orderNumber, o.status)
            FROM OrderEntity o
            WHERE o.userName = :userName
            """)
    List<OrderSummary> findByUserName(String userName);

    Optional<OrderEntity> findByUserNameAndOrderNumber(String userName, String orderNumber);
}
