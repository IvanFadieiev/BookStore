package project.bookstore.service;

import project.bookstore.dto.shoppingcart.CartItemRequestDto;
import project.bookstore.dto.shoppingcart.CartItemUpdateDto;
import project.bookstore.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart();

    void addBooksToShoppingCart(CartItemRequestDto cartItemRequestDto);

    void updateBookQuantityById(Long id, CartItemUpdateDto cartItemUpdateDto);

    void deleteBookById(Long id);
}
