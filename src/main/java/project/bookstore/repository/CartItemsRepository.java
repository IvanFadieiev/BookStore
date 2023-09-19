package project.bookstore.repository;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.CartItem;

public interface CartItemsRepository extends JpaRepository<CartItem, Long> {
    Set<CartItem> getCartItemsByShoppingCartId(Long id);

    Optional<CartItem> findCartItemById(Long id);

    void deleteCartItemById(Long id);
}
