package project.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    private static final Long VALID_ID = 1L;
    private static final User BOB = new User().setEmail("bob@mail.com")
            .setPassword("password")
            .setFirstName("Bob")
            .setLastName("Johnson")
            .setShippingAddress("test");
    private static final String CLEAR_USER_TABLE
            = "classpath:database/scripts/users/clear-users-table.sql";
    private static final String ADD_USER
            = "classpath:database/scripts/users/add-user-to-users-table.sql";
    private static final String ADD_SHOPPING_CART
            = "classpath:database/scripts/shoppingcart/"
            + "add-shopping-cart-to-shopping-cart-table.sql";
    private static final String CLEAR_SHOPPING_CART_TABLE
            = "classpath:database/scripts/shoppingcart/clear-shopping-cart-table.sql";

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Verify getShoppingCartByUserId() method works")
    @Sql(scripts = {
            CLEAR_USER_TABLE, ADD_USER, ADD_SHOPPING_CART
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_USER_TABLE, CLEAR_SHOPPING_CART_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getShoppingCartByUserId_ValidUserId_ReturnsShoppingCart() {
        Optional<ShoppingCart> shoppingCartByUserId
                = shoppingCartRepository.getShoppingCartByUserId(VALID_ID);
        assertTrue(shoppingCartByUserId.isPresent());
        User shoppingCartUser = shoppingCartByUserId.get().getUser();
        reflectionEquals(BOB, shoppingCartUser, "id");
    }
}
