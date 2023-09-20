package project.bookstore.service;

import project.bookstore.dto.shoppingcart.CartItemRequestDto;
import project.bookstore.dto.shoppingcart.CartItemUpdateDto;
import project.bookstore.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart();

    void addCartItemToShoppingCart(CartItemRequestDto cartItemRequestDto);

    void updateCartItemQuantityById(Long id, CartItemUpdateDto cartItemUpdateDto);

    void deleteCartItemById(Long id);
}
