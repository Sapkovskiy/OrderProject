package org.orderservice.repository;

import org.orderservice.model.Order;
import org.orderservice.model.OrderItem;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

    @Query("SELECT o.id, o.order_number, o.total_amount, o.order_date, o.customer_name, " +
           "o.delivery_address, o.payment_type, o.delivery_type " +
           "FROM public.orders o")
    List<Order> findAllOrders();

    @Query("SELECT * FROM public.order_items WHERE order_id = :orderId")
    List<OrderItem> findOrderItemsByOrderId(int orderId);

    @Query("SELECT * FROM public.orders WHERE order_date::DATE = :date AND total_amount > :minTotal")
    List<Order> findByDateAndTotal(LocalDate date, BigDecimal minTotal);

    @Query("""
        SELECT * FROM public.orders o
        WHERE o.id NOT IN (
            SELECT oi.order_id FROM public.order_items oi WHERE oi.product_name = :productName
        )
        AND o.order_date BETWEEN :startDate AND :endDate
    """)
    List<Order> findOrdersWithoutProductAndInPeriod(String productName, LocalDate startDate, LocalDate endDate);
}
