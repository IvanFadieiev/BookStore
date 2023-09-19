package project.bookstore.service.impl;

import jakarta.transaction.Transactional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookstore.dto.shoppingcart.CartItemRequestDto;
import project.bookstore.dto.shoppingcart.CartItemUpdateDto;
import project.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.CartItemMapper;
import project.bookstore.mapper.ShoppingCartMapper;
import project.bookstore.model.Book;
import project.bookstore.model.CartItem;
import project.bookstore.model.ShoppingCart;
import project.bookstore.repository.BookRepository;
import project.bookstore.repository.CartItemsRepository;
import project.bookstore.repository.ShoppingCartRepository;
import project.bookstore.service.ShoppingCartService;
import project.bookstore.service.UserService;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final CartItemsRepository cartItemsRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartResponseDto getShoppingCart() {
        ShoppingCart shoppingCart = getCurrentShoppingCart();
        ShoppingCartResponseDto dto = shoppingCartMapper.toDto(shoppingCart);
        dto.setCartItems(cartItemsRepository
                .getCartItemsByShoppingCartId(shoppingCart.getId()).stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet()));
        return dto;
    }

    @Override
    public void addBooksToShoppingCart(CartItemRequestDto cartItemRequestDto) {
        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                .orElseThrow(()
                        -> new EntityNotFoundException("Book with provided id doesn't exists"));
        CartItem cartItem = new CartItem();
        ShoppingCart shoppingCart = getCurrentShoppingCart();
        cartItem.setBook(book);
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        cartItem.setShoppingCart(shoppingCart);
        cartItemsRepository.save(cartItem);
    }

    @Override
    public void updateBookQuantityById(Long id, CartItemUpdateDto cartItemUpdateDto) {
        CartItem cartItem = cartItemsRepository.findCartItemById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("CardItem with provided id doesn't exists"));
        cartItem.setQuantity(cartItemUpdateDto.getQuantity());
        cartItemsRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void deleteBookById(Long id) {
        cartItemsRepository.findCartItemById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("CardItem with provided id doesn't exists"));
        cartItemsRepository.deleteCartItemById(id);
    }

    private ShoppingCart getCurrentShoppingCart() {
        return shoppingCartRepository
                .getShoppingCartByUserId(userService.getAuthentificatedUser().getId())
                .orElseThrow(()
                        -> new EntityNotFoundException("Shopping Cart with "
                        + "provided user's id doesn't exists"));
    }
}
