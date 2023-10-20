package project.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import project.bookstore.dto.shoppingcart.CartItemRequestDto;
import project.bookstore.dto.shoppingcart.CartItemResponseDto;
import project.bookstore.dto.shoppingcart.CartItemUpdateDto;
import project.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.CartItemMapper;
import project.bookstore.mapper.ShoppingCartMapper;
import project.bookstore.model.Book;
import project.bookstore.model.CartItem;
import project.bookstore.model.Category;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;
import project.bookstore.repository.BookRepository;
import project.bookstore.repository.CartItemsRepository;
import project.bookstore.repository.ShoppingCartRepository;
import project.bookstore.service.impl.ShoppingCartServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = -10L;
    private static final int BOOK_QUANTITY = 10;
    private static final User VALID_USER = new User()
            .setId(1L)
            .setFirstName("Bob")
            .setLastName("Johnson")
            .setEmail("bob@mail.com")
            .setPassword("password")
            .setShippingAddress("shipping address");
    private static final User IN_VALID_USER = new User()
            .setId(-10L)
            .setFirstName("Invalid")
            .setLastName("Invalid")
            .setEmail("Invalid")
            .setPassword("Invalid")
            .setShippingAddress("Invalid");
    private static final Book BOOK = new Book()
            .setId(1L)
            .setTitle("Harry Potter")
            .setAuthor("J.K.Rolling")
            .setIsbn("0-2936-9647-0")
            .setPrice(BigDecimal.valueOf(1000))
            .setCategories(Set.of(new Category(1L)));
    private static final CartItem CART_ITEM = new CartItem()
            .setId(1L)
            .setBook(BOOK)
            .setQuantity(10);
    private static final CartItem CART_ITEM_WITHOUT_ID = new CartItem()
            .setBook(BOOK)
            .setQuantity(10);
    private static final CartItemRequestDto CART_ITEM_REQUEST_DTO
            = new CartItemRequestDto()
            .setBookId(VALID_ID)
            .setQuantity(BOOK_QUANTITY);
    private static final CartItemUpdateDto CART_ITEM_UPDATE_DTO
            = new CartItemUpdateDto()
            .setQuantity(BOOK_QUANTITY);
    private static final CartItemRequestDto CART_ITEM_REQUEST_DTO_INVALID_BOOK_ID
            = new CartItemRequestDto()
            .setBookId(INVALID_ID)
            .setQuantity(BOOK_QUANTITY);
    private static final CartItemResponseDto CART_ITEM_RESPONSE
            = new CartItemResponseDto()
            .setId(1L)
            .setBookId(BOOK.getId())
            .setBookTitle(BOOK.getTitle())
            .setQuantity(10);
    private static final Set<CartItem> CART_ITEMS = Set.of(CART_ITEM);
    private static final Set<CartItemResponseDto> CART_ITEMS_RESPONSE
            = Set.of(CART_ITEM_RESPONSE);
    private static final ShoppingCart SHOPPING_CART = new ShoppingCart()
            .setId(1L)
            .setUser(VALID_USER)
            .setCartItems(CART_ITEMS);
    private static final ShoppingCart SHOPPING_CART_WITHOUT_ID = new ShoppingCart()
            .setUser(VALID_USER);
    private static final ShoppingCartResponseDto SHOPPING_CART_RESPONSE_DTO
            = new ShoppingCartResponseDto()
            .setId(1L)
            .setUserId(VALID_USER.getId())
            .setCartItems(CART_ITEMS_RESPONSE);
    private static final Authentication AUTHENTICATION =
            mock(Authentication.class);
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CartItemsRepository cartItemsRepository;
    @Mock
    private CartItemMapper cartItemMapper;

    @Test
    @DisplayName("Verify getShoppingCart() method works")
    public void getShoppingCart_ReturnsValidShoppingCart() {
        when(shoppingCartMapper.toDto(SHOPPING_CART)).thenReturn(SHOPPING_CART_RESPONSE_DTO);
        SecurityContextHolder.getContext().setAuthentication(AUTHENTICATION);
        when(AUTHENTICATION.getPrincipal()).thenReturn(VALID_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(VALID_ID))
                .thenReturn(Optional.ofNullable(SHOPPING_CART));
        when(cartItemsRepository.getCartItemsByShoppingCartId(VALID_ID))
                .thenReturn(CART_ITEMS);
        when(cartItemMapper.toDto(CART_ITEM)).thenReturn(CART_ITEM_RESPONSE);

        ShoppingCartResponseDto shoppingCart = shoppingCartService.getShoppingCart();

        assertEquals(shoppingCart, SHOPPING_CART_RESPONSE_DTO);
        verify(shoppingCartMapper, Mockito.times(1))
                .toDto(SHOPPING_CART);
        verify(AUTHENTICATION, Mockito.times(1))
                .getPrincipal();
        verify(shoppingCartRepository, Mockito.times(1))
                .getShoppingCartByUserId(VALID_ID);
        verify(cartItemsRepository, Mockito.times(1))
                .getCartItemsByShoppingCartId(VALID_ID);
        verify(cartItemMapper, Mockito.times(1))
                .toDto(CART_ITEM);
    }

    @Test
    @DisplayName("Verify getShoppingCart() throws exception")
    public void getShoppingCart_ThrowsException() {
        SecurityContextHolder.getContext().setAuthentication(AUTHENTICATION);
        when(AUTHENTICATION.getPrincipal()).thenReturn(IN_VALID_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(INVALID_ID))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getShoppingCart());

        String actual = exception.getMessage();
        String expected = "Shopping Cart with provided user's id: "
                + INVALID_ID
                + "  doesn't exists";

        assertEquals(actual, expected);
        verify(AUTHENTICATION, Mockito.times(4))
                .getPrincipal();
        verify(shoppingCartRepository, Mockito.times(1))
                .getShoppingCartByUserId(INVALID_ID);
    }

    @Test
    @DisplayName("Verify addCartItemToShoppingCart() method works")
    public void addCartItemToShoppingCart_ReturnsResponseStatusAccepted() {
        when(bookRepository.findById(VALID_ID)).thenReturn(Optional.ofNullable(BOOK));
        SecurityContextHolder.getContext().setAuthentication(AUTHENTICATION);
        when(AUTHENTICATION.getPrincipal()).thenReturn(VALID_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(VALID_ID))
                .thenReturn(Optional.ofNullable(SHOPPING_CART));
        when(cartItemsRepository.save(CART_ITEM_WITHOUT_ID)).thenReturn(CART_ITEM);

        shoppingCartService.addCartItemToShoppingCart(CART_ITEM_REQUEST_DTO);

        verify(bookRepository, Mockito.times(1))
                .findById(VALID_ID);
        verify(AUTHENTICATION, Mockito.times(5))
                .getPrincipal();
        verify(shoppingCartRepository, Mockito.times(1))
                .getShoppingCartByUserId(VALID_ID);
        verify(cartItemsRepository, Mockito.times(1))
                .save(CART_ITEM_WITHOUT_ID);
    }

    @Test
    @DisplayName("Verify addCartItemToShoppingCart() throws exception with invalid id")
    public void addCartItemToShoppingCart_InValidBookId_ThrowsException() {
        when(bookRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService
                        .addCartItemToShoppingCart(CART_ITEM_REQUEST_DTO_INVALID_BOOK_ID));

        String actualMessage = exception.getMessage();
        String expected = "Book with provided id: " + INVALID_ID + " doesn't exists";

        assertEquals(actualMessage, expected);
        verify(bookRepository, Mockito.times(1))
                .findById(INVALID_ID);
    }

    @Test
    @DisplayName("Verify updateCartItemQuantityById() method works")
    public void updateCartItemQuantityById_ValidIdAndValidDto_ReturnsResponseNoContent() {
        SecurityContextHolder.getContext().setAuthentication(AUTHENTICATION);
        when(AUTHENTICATION.getPrincipal()).thenReturn(VALID_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(VALID_ID))
                .thenReturn(Optional.ofNullable(SHOPPING_CART));
        when(cartItemsRepository.getCartItemByIdAndUserId(VALID_ID, VALID_ID))
                .thenReturn(Optional.ofNullable(CART_ITEM));
        when(cartItemsRepository.save(CART_ITEM)).thenReturn(CART_ITEM);

        shoppingCartService.updateCartItemQuantityById(VALID_ID, CART_ITEM_UPDATE_DTO);

        verify(AUTHENTICATION, Mockito.times(3))
                .getPrincipal();
        verify(cartItemsRepository, Mockito.times(1))
                .getCartItemByIdAndUserId(VALID_ID, VALID_ID);
        verify(cartItemsRepository, Mockito.times(1))
                .save(CART_ITEM);
    }

    @Test
    @DisplayName("Verify updateCartItemQuantityById() throws exception with invalid id")
    public void updateCartItemQuantityById_InValidId_ThrowsException() {
        SecurityContextHolder.getContext().setAuthentication(AUTHENTICATION);
        when(AUTHENTICATION.getPrincipal()).thenReturn(VALID_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(VALID_ID))
                .thenReturn(Optional.ofNullable(SHOPPING_CART));
        when(cartItemsRepository.getCartItemByIdAndUserId(INVALID_ID, VALID_ID))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.updateCartItemQuantityById(INVALID_ID,
                        CART_ITEM_UPDATE_DTO));

        String actualMessage = exception.getMessage();
        String expected = "CardItem with provided id: " + INVALID_ID + " doesn't exists";

        assertEquals(actualMessage, expected);
        verify(AUTHENTICATION, Mockito.times(2))
                .getPrincipal();
        verify(shoppingCartRepository, Mockito.times(1))
                .getShoppingCartByUserId(VALID_ID);
        verify(cartItemsRepository, Mockito.times(1))
                .getCartItemByIdAndUserId(INVALID_ID, VALID_ID);
    }

    @Test
    @DisplayName("Verify deleteCartItemById() method works")
    public void deleteCartItemById_ValidIdAndValidDto_ReturnsResponseNoContent() {
        SecurityContextHolder.getContext().setAuthentication(AUTHENTICATION);
        when(AUTHENTICATION.getPrincipal()).thenReturn(VALID_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(VALID_ID))
                .thenReturn(Optional.ofNullable(SHOPPING_CART));
        when(cartItemsRepository.getCartItemByIdAndUserId(VALID_ID, VALID_ID))
                .thenReturn(Optional.ofNullable(CART_ITEM));

        shoppingCartService.deleteCartItemById(VALID_ID);

        verify(AUTHENTICATION, Mockito.times(7))
                .getPrincipal();
        verify(cartItemsRepository, Mockito.times(1))
                .getCartItemByIdAndUserId(VALID_ID, VALID_ID);
    }

    @Test
    @DisplayName("Verify deleteCartItemById() throws exception with invalid id")
    public void deleteCartItemById_InValidId_ThrowsException() {
        SecurityContextHolder.getContext().setAuthentication(AUTHENTICATION);
        when(AUTHENTICATION.getPrincipal()).thenReturn(VALID_USER);
        when(shoppingCartRepository.getShoppingCartByUserId(VALID_ID))
                .thenReturn(Optional.ofNullable(SHOPPING_CART));
        when(cartItemsRepository.getCartItemByIdAndUserId(INVALID_ID, VALID_ID))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.deleteCartItemById(INVALID_ID));

        String actualMessage = exception.getMessage();
        String expected = "CardItem with provided id: " + INVALID_ID + " doesn't exists";

        assertEquals(actualMessage, expected);
        verify(AUTHENTICATION, Mockito.times(6))
                .getPrincipal();
        verify(shoppingCartRepository, Mockito.times(1))
                .getShoppingCartByUserId(VALID_ID);
        verify(cartItemsRepository, Mockito.times(1))
                .getCartItemByIdAndUserId(INVALID_ID, VALID_ID);
    }

    @Test
    @DisplayName("Verify createNewShoppingCartForNewUser() method works")
    public void createNewShoppingCartForNewUser_ValidUser() {
        when(shoppingCartRepository.save(SHOPPING_CART_WITHOUT_ID))
                .thenReturn(SHOPPING_CART_WITHOUT_ID);

        shoppingCartService.createNewShoppingCartForNewUser(VALID_USER);

        verify(shoppingCartRepository, Mockito.times(1))
                .save(SHOPPING_CART_WITHOUT_ID);
    }
}

