package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CategoryRequestDto;
import project.bookstore.repository.BookRepository;
import project.bookstore.service.BookService;
import project.bookstore.service.CategoryService;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/categories")
@Tag(name = "Categories management", description = "Endpoints for managing categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get a category by id", description
            = "Get a category by specified id")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all categories", description
            = "Get a list of all categories")
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}/books")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get books by provided category id", description
            = "Get a list of all categories")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.findAllByCategoryId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Create a new category", description
            = "Create a new category")
    public CategoryDto createCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto){
        return categoryService.save(categoryRequestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update a book by id", description
            = "Update existing book by specified id")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        return categoryService.update(id, categoryRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete a category by id", description
            = "Delete a category by specified id")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
