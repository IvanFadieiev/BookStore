package project.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "cart_items")
@SQLDelete(sql = "UPDATE cart_items SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart.id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ShoppingCart shoppingCart;
    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Book book;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
