package project.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryRequestDto {
    @NotBlank
    @Size(min = 1)
    private String name;
    @NotBlank
    @Size(min = 1)
    private String description;
}
