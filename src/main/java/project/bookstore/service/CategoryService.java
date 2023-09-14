package project.bookstore.service;

import project.bookstore.dto.category.CategoryDto;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.category.CategoryRequestDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);
    CategoryDto getById(Long id);
    CategoryDto save(CategoryRequestDto categoryRequestDto);
    CategoryDto update(Long id, CategoryRequestDto categoryRequestDto);
    void deleteById(Long id);
}
