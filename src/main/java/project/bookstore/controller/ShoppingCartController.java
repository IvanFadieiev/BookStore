package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.shoppingcart.CartItemRequestDto;
import project.bookstore.dto.shoppingcart.CartItemUpdateDto;
import project.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import project.bookstore.service.ShoppingCartService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get a shopping cart", description
            = "Get a shopping cart content")
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Add cart item to shopping cart", description
            = "Add cart item to shopping cart")
    public void addCartItemToShoppingCart(@RequestBody @Valid
                                       CartItemRequestDto cartItemRequestDto) {
        shoppingCartService.addCartItemToShoppingCart(cartItemRequestDto);
    }

    @PutMapping("/cart-item/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update quantity of a cart item", description
            = "Update quantity of a cart item by provided id")
    public void updateCartItemQuantityById(@PathVariable Long id,
                               @RequestBody @Valid CartItemUpdateDto cartItemUpdateDto) {
        shoppingCartService.updateCartItemQuantityById(id, cartItemUpdateDto);
    }

    @DeleteMapping("/cart-item/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a cart item by id", description
            = "Delete a cart item by provided id from shopping cart")
    public void deleteCartItemById(@PathVariable Long id) {
        shoppingCartService.deleteCartItemById(id);
    }
}
