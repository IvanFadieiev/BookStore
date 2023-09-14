package project.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CategoryRequestDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.CategoryMapper;
import project.bookstore.model.Category;
import project.bookstore.repository.CategoryRepository;
import project.bookstore.service.CategoryService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category by provided "
                        + id + " doesn't exists")));
    }

    @Override
    public CategoryDto save(CategoryRequestDto categoryRequestDto) {
        return categoryMapper.toDto(categoryRepository
                .save(categoryMapper.toModel(categoryRequestDto)));
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto categoryRequestDto) {
        Category updatedCategory = categoryMapper.toModel(categoryRequestDto);
        updatedCategory.setId(id);
        return categoryMapper.toDto(categoryRepository.save(updatedCategory));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
