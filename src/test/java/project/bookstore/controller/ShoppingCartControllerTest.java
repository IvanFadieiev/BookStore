package project.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.bookstore.dto.shoppingcart.CartItemRequestDto;
import project.bookstore.dto.shoppingcart.CartItemResponseDto;
import project.bookstore.dto.shoppingcart.CartItemUpdateDto;
import project.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import project.bookstore.model.Book;
import project.bookstore.model.Category;
import project.bookstore.model.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    private static final String VALID_USER_EMAIL = "bob@mail.com";
    private static final String CLEAR_USER_TABLE
            = "classpath:database/scripts/users/clear-users-table.sql";
    private static final String CLEAR_SHOPPING_CART_TABLE
            = "classpath:database/scripts/shoppingcart/clear-shopping-cart-table.sql";
    private static final String CLEAR_CART_ITEM_TABLE
            = "classpath:database/scripts/cartitems/clear-cart-items-table.sql";
    private static final String CLEAR_BOOKS_TABLE
            = "classpath:database/scripts/books/clear-books-table.sql";
    private static final String ADD_USER
            = "classpath:database/scripts/users/add-user-to-users-table.sql";
    private static final String ADD_BOOK
            = "classpath:database/scripts/books/add-book-to-books-table.sql";
    private static final String ADD_SHOPPING_CART
            = "classpath:database/scripts/shoppingcart/"
            + "add-shopping-cart-to-shopping-cart-table.sql";
    private static final String ADD_CART_ITEM
            = "classpath:database/scripts/cartitems/add-cart-item-to-cart-items-table.sql";
    private static final Long VALID_ID = 1L;
    private static final int BOOK_QUANTITY = 5;

    private static final User VALID_USER = new User()
            .setId(1L)
            .setFirstName("Bob")
            .setLastName("Johnson")
            .setEmail("bob@mail.com")
            .setPassword("password")
            .setShippingAddress("shipping address");
    private static final Book BOOK = new Book()
            .setId(1L)
            .setTitle("Meditations")
            .setAuthor("Marcus Aurelius")
            .setIsbn("978-4-2596-7831-9")
            .setPrice(BigDecimal.valueOf(1000))
            .setCategories(Set.of(new Category(1L)));
    private static final CartItemRequestDto CART_ITEM_REQUEST_DTO
            = new CartItemRequestDto()
            .setBookId(VALID_ID)
            .setQuantity(BOOK_QUANTITY);
    private static final CartItemUpdateDto CART_ITEM_UPDATE_DTO
            = new CartItemUpdateDto()
            .setQuantity(BOOK_QUANTITY);
    private static final CartItemResponseDto CART_ITEM_RESPONSE
            = new CartItemResponseDto()
            .setId(1L)
            .setBookId(BOOK.getId())
            .setBookTitle(BOOK.getTitle())
            .setQuantity(1);
    private static final Set<CartItemResponseDto> CART_ITEMS_RESPONSE
            = Set.of(CART_ITEM_RESPONSE);
    private static final ShoppingCartResponseDto SHOPPING_CART_RESPONSE_DTO
            = new ShoppingCartResponseDto()
            .setId(1L)
            .setUserId(VALID_USER.getId())
            .setCartItems(CART_ITEMS_RESPONSE);
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithUserDetails(VALID_USER_EMAIL)
    @Test
    @DisplayName("Verify getShoppingCart() method works")
    @Sql(scripts = {
            CLEAR_USER_TABLE, CLEAR_BOOKS_TABLE,
            ADD_USER, ADD_BOOK, ADD_SHOPPING_CART,
            ADD_CART_ITEM
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_USER_TABLE, CLEAR_SHOPPING_CART_TABLE,
            CLEAR_CART_ITEM_TABLE, CLEAR_BOOKS_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getShoppingCart_ReturnsShoppingCartDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        ShoppingCartResponseDto shoppingCartResponseDto = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), ShoppingCartResponseDto.class);

        assertNotNull(shoppingCartResponseDto);
        assertEquals(shoppingCartResponseDto, SHOPPING_CART_RESPONSE_DTO);
    }

    @WithUserDetails(VALID_USER_EMAIL)
    @Test
    @DisplayName("Verify addCartItemToShoppingCart() method works")
    @Sql(scripts = {
            CLEAR_USER_TABLE, ADD_USER,
            ADD_BOOK, ADD_SHOPPING_CART
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_USER_TABLE, CLEAR_SHOPPING_CART_TABLE,
            CLEAR_CART_ITEM_TABLE, CLEAR_BOOKS_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addCartItemToShoppingCart_ValidRequestDto_ResponseStatusAccepted()
            throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(CART_ITEM_REQUEST_DTO);
        mockMvc.perform(post("/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @WithUserDetails(VALID_USER_EMAIL)
    @Test
    @DisplayName("Verify updateCartItemQuantityById() method works")
    @Sql(scripts = {
            CLEAR_USER_TABLE, ADD_USER,
            ADD_SHOPPING_CART, ADD_CART_ITEM
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_USER_TABLE, CLEAR_SHOPPING_CART_TABLE,
            CLEAR_CART_ITEM_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCartItemQuantityById_ValidRequestDtoAndID_ResponseStatusNoContent()
            throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(CART_ITEM_UPDATE_DTO);
        mockMvc.perform(put("/cart/cart-item/" + VALID_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithUserDetails(VALID_USER_EMAIL)
    @Test
    @DisplayName("Verify deleteCartItemById() method works")
    @Sql(scripts = {
            CLEAR_USER_TABLE, ADD_USER,
            ADD_SHOPPING_CART, ADD_CART_ITEM
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_USER_TABLE, CLEAR_SHOPPING_CART_TABLE,
            CLEAR_CART_ITEM_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCartItemById_ValidID_ResponseStatusNoContent()
            throws Exception {
        mockMvc.perform(delete("/cart/cart-item/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
