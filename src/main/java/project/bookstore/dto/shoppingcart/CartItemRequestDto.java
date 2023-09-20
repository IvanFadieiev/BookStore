package project.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @Min(1)
    private Long bookId;
    @NotNull
    @Min(1)
    private Integer quantity;
}
