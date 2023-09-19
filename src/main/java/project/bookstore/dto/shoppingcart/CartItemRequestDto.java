package project.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @Min(1)
    private Long bookId;
    @Min(1)
    private Integer quantity;
}
