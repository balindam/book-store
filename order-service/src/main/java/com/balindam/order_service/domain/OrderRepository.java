package com.balindam.order_service.domain;

import com.balindam.order_service.domain.models.OrderSummary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query(
            """
            SELECT new com.balindam.order_service.domain.models.OrderSummary(o.orderNumber, o.status)
            FROM OrderEntity o
            WHERE o.userName = :userName
            """)
    List<OrderSummary> findByUserName(String userName);

    @Query(
            """
            SELECT distinct o
            FROM OrderEntity o left join fetch o.items
            WHERE o.userName = :userName AND o.orderNumber = :orderNumber
            """)
    Optional<OrderEntity> findByUserNameAndOrderNumber(String userName, String orderNumber);
}
