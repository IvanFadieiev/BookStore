package project.bookstore.service;

import java.util.Set;
import project.bookstore.dto.order.OrderItemResponseDto;
import project.bookstore.dto.order.OrderRequestDto;
import project.bookstore.dto.order.OrderResponseDto;
import project.bookstore.dto.order.OrderStatusRequestDto;

public interface OrderService {
    void createNewOrder(OrderRequestDto orderRequestDto);

    Set<OrderResponseDto> getOrders();

    Set<OrderItemResponseDto> getOrderItemsByOrderId(Long id);

    OrderItemResponseDto getOrderItemByOrderIdAndOrderItemId(Long orderId, Long orderItemId);

    void updateOrderStatus(Long id, OrderStatusRequestDto status);
}
