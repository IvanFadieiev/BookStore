package project.bookstore.repository;

import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Set<Order> findAllOrdersByUserId(Long id);

    Optional<Order> getOrderById(Long id);
}
