package project.bookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookstore.dto.order.OrderItemResponseDto;
import project.bookstore.dto.order.OrderRequestDto;
import project.bookstore.dto.order.OrderResponseDto;
import project.bookstore.dto.order.OrderStatusRequestDto;
import project.bookstore.enums.Status;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.OrderItemMapper;
import project.bookstore.mapper.OrderMapper;
import project.bookstore.model.CartItem;
import project.bookstore.model.Order;
import project.bookstore.model.OrderItem;
import project.bookstore.repository.CartItemsRepository;
import project.bookstore.repository.OrderItemRepository;
import project.bookstore.repository.OrderRepository;
import project.bookstore.service.OrderService;
import project.bookstore.service.ShoppingCartService;
import project.bookstore.service.UserService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final CartItemsRepository cartItemsRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public void createNewOrder(OrderRequestDto orderRequestDto) {
        Order filledOrder = fillOrder(orderRequestDto);
        orderRepository.save(filledOrder);
    }

    @Override
    public Set<OrderResponseDto> getOrders() {
        return orderRepository.findAllOrdersByUserId(userService.getAuthentificatedUser()
                        .getId()).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<OrderItemResponseDto> getOrderItemsByOrderId(Long id) {
        return orderItemRepository.findOrderItemsByOrderId(id).stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemResponseDto getOrderItemByOrderIdAndOrderItemId(Long orderId,
                                                                    Long orderItemId) {
        return orderItemRepository.findOrderItemByOrderIdAndOrderItemId(orderId, orderItemId)
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Order item by provided id: "
                        + orderItemId + " doesn't exists"));
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatusRequestDto status) {
        Order order = getOrderById(id);
        order.setStatus(Status.valueOf(status.status()));
        orderRepository.save(order);
    }

    private OrderItem createOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        return orderItem;
    }

    private Order fillOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setUser(userService.getAuthentificatedUser());
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequestDto.shippingAddress());
        Set<OrderItem> orderItems = cartItemsRepository
                .getCartItemsByShoppingCartId(shoppingCartService
                        .getShoppingCart()
                        .getId())
                .stream()
                .map(e -> createOrderItem(e, order))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        order.setTotal(order.getOrderItems().stream()
                .map(e -> e.getBook().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return order;
    }

    private Order getOrderById(Long id) {
        return orderRepository.getOrderById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order by provided id "
                        + id + " doesn't exists"));
    }
}
