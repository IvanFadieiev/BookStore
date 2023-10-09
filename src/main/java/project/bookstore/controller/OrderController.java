package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.order.OrderItemResponseDto;
import project.bookstore.dto.order.OrderRequestDto;
import project.bookstore.dto.order.OrderResponseDto;
import project.bookstore.dto.order.OrderStatusRequestDto;
import project.bookstore.service.OrderService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders")
@Tag(name = "Order management", description = "Endpoints for managing orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get an order history", description
            = "Get an order history")
    public Set<OrderResponseDto> getOrder() {
        return orderService.getOrders();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Create a new order", description
            = "Create a new order with provided shipping address")
    public void createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        orderService.createNewOrder(orderRequestDto);
    }

    @GetMapping("{orderId}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get an order items", description
            = "Get an order items by provided order id")
    public Set<OrderItemResponseDto> getOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderService.getOrderItemsByOrderId(orderId);
    }

    @GetMapping("{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get an order's item", description
            = "Get an specific item from order by provided id")
    public OrderItemResponseDto getOrderSpecificItem(@PathVariable Long orderId, @PathVariable
                                                     Long itemId) {
        return orderService.getOrderItemByOrderIdAndOrderItemId(orderId, itemId);
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update order status", description
            = "Update order status by provided id")
    public void updateOrderStatus(@PathVariable Long id,
                                  @RequestBody OrderStatusRequestDto orderStatusRequestDto) {
        orderService.updateOrderStatus(id, orderStatusRequestDto);
    }
}
