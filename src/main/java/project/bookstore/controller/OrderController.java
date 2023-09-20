package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.order.OrderItemResponseDto;
import project.bookstore.dto.order.OrderItemsResponseDto;
import project.bookstore.dto.order.OrderRequestDto;
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
    public void getOrder() {

    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Create a new order", description
            = "Create a new order with provided shipping address")
    public void createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {

    }

    @GetMapping("{orderId}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get an order items", description
            = "Get an order items by provided order id")
    public OrderItemsResponseDto getOrderItems(@PathVariable Long orderId) {
        return null;
    }

    @GetMapping("{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get an order's item", description
            = "Get an specific item from order by provided id")
    public OrderItemResponseDto getOrderSpecificItem(@PathVariable Long orderId,
                                                     Long itemId) {
        return null;
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update order status", description
            = "Update order status by provided id")
    public void updateOrderStatus(@RequestBody OrderStatusRequestDto orderStatusRequestDto) {

    }
 }
