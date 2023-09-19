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
    @Operation(summary = "Add books to shopping cart", description
            = "Add books to shopping cart")
    public void addBooksToShoppingCart(@RequestBody @Valid
                                       CartItemRequestDto cartItemRequestDto) {
        shoppingCartService.addBooksToShoppingCart(cartItemRequestDto);
    }

    @PutMapping("/cart-item/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update quantity of a book", description
            = "Update quantity of a book by provided id")
    public void updateBookById(@PathVariable Long id,
                               @RequestBody @Valid CartItemUpdateDto cartItemUpdateDto) {
        shoppingCartService.updateBookQuantityById(id, cartItemUpdateDto);
    }

    @DeleteMapping("/cart-item/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Delete a book by id", description
            = "Delete a book by provided id from shopping cart")
    public void deleteBookById(@PathVariable Long id) {
        shoppingCartService.deleteBookById(id);
    }
}
