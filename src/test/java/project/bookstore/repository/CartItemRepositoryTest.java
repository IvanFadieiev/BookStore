package project.bookstore.repository;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import project.bookstore.model.Book;
import project.bookstore.model.CartItem;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartItemRepositoryTest {
    private static final Long VALID_SHOPPING_CART_ID = 1L;
    private static final Long VALID_USER_ID = 1L;
    private static final Long VALID_CART_ITEM_ID = 1L;
    private static final Book BOOK = new Book()
            .setTitle("Meditations")
            .setAuthor("Marcus Aurelius")
            .setIsbn("978-4-2596-7831-9")
            .setPrice(BigDecimal.valueOf(1000))
            .setDescription("Description")
            .setCoverImage("Cover image");

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Test
    @DisplayName("Verify getCartItemsByShoppingCartId() method works")
    @Sql(scripts = {
            "classpath:database/scripts/books/clear-books-table.sql",
            "classpath:database/scripts/books/add-book-to-books-table.sql",
            "classpath:database/scripts/shoppingcart/add-shopping-cart-to-shopping-cart-table.sql",
            "classpath:database/scripts/cartitems/add-cart-item-to-cart-items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/books/clear-books-table.sql",
            "classpath:database/scripts/shoppingcart/clear-shopping-cart-table.sql",
            "classpath:database/scripts/cartitems/clear-cart-items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCartItemsByShoppingCartId_ValidShoppingCartId_ReturnsCartItemsSet() {
        Set<CartItem> items = cartItemsRepository
                .getCartItemsByShoppingCartId(VALID_SHOPPING_CART_ID);
       reflectionEquals(BOOK, items.iterator().next().getBook(), "id");
    }

    @Test
    @DisplayName("Verify getCartItemByIdAndUserId() method works")
    @Sql(scripts = {
            "classpath:database/scripts/books/clear-books-table.sql",
            "classpath:database/scripts/users/clear-users-table.sql",
            "classpath:database/scripts/books/add-book-to-books-table.sql",
            "classpath:database/scripts/users/add-user-to-users-table.sql",
            "classpath:database/scripts/shoppingcart/add-shopping-cart-to-shopping-cart-table.sql",
            "classpath:database/scripts/cartitems/add-cart-item-to-cart-items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/books/clear-books-table.sql",
            "classpath:database/scripts/users/clear-users-table.sql",
            "classpath:database/scripts/shoppingcart/clear-shopping-cart-table.sql",
            "classpath:database/scripts/cartitems/clear-cart-items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCartItemByIdAndUserId_ValidItemIdAndUserId_ReturnsCartItem() {
        Optional<CartItem> cartItemByIdAndUserId
                = cartItemsRepository.getCartItemByIdAndUserId(VALID_USER_ID, VALID_CART_ITEM_ID);
        assertTrue(cartItemByIdAndUserId.isPresent());
        reflectionEquals(cartItemByIdAndUserId.get().getBook(), BOOK, "id");
    }
}
