package project.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import project.bookstore.model.ShoppingCart;
import project.bookstore.model.User;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    private static final Long VALID_ID = 1L;
    private static final User BOB = new User().setEmail("bob@mail.com")
            .setPassword("password")
            .setFirstName("Bob")
            .setLastName("Johnson")
            .setShippingAddress("test");

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Verify getShoppingCartByUserId() method works")
    @Sql(scripts = {
            "classpath:database/scripts/users/clear-users-table.sql",
            "classpath:database/scripts/users/add-user-to-users-table.sql",
            "classpath:database/scripts/shoppingcart/add-shopping-cart-to-shopping-cart-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/users/clear-users-table.sql",
            "classpath:database/scripts/shoppingcart/clear-shopping-cart-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getShoppingCartByUserId_ValidUserId_ReturnsShoppingCart() {
        Optional<ShoppingCart> shoppingCartByUserId
                = shoppingCartRepository.getShoppingCartByUserId(VALID_ID);
        assertTrue(shoppingCartByUserId.isPresent());
        User shoppingCartUser = shoppingCartByUserId.get().getUser();
        reflectionEquals(BOB, shoppingCartUser, "id");
    }
}
