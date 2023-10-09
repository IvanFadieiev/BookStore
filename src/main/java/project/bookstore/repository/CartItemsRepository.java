package project.bookstore.repository;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.bookstore.model.CartItem;

public interface CartItemsRepository extends JpaRepository<CartItem, Long> {
    Set<CartItem> getCartItemsByShoppingCartId(Long id);

    @Query("""
            FROM CartItem c
            LEFT JOIN ShoppingCart sc
            ON c.shoppingCart.id = sc.id
            LEFT JOIN User u
            ON sc.user.id =:userId
            WHERE c.id =:id
            """)
    Optional<CartItem> getCartItemByIdAndUserId(Long id, Long userId);
}
