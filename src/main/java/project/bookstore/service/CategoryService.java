package project.bookstore.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CategoryRequestDto;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto categoryRequestDto);

    CategoryDto update(Long id, CategoryRequestDto categoryRequestDto);

    void deleteById(Long id);
}
