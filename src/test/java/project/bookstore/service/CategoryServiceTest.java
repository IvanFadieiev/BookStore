package project.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CategoryRequestDto;
import project.bookstore.exception.EntityNotFoundException;
import project.bookstore.mapper.CategoryMapper;
import project.bookstore.model.Category;
import project.bookstore.repository.CategoryRepository;
import project.bookstore.service.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    private static final CategoryRequestDto CATEGORY_REQUEST_DTO = new CategoryRequestDto()
            .setName("Horror")
            .setDescription("Description");
    private static final Category CATEGORY_MODEL = new Category()
            .setName("Horror")
            .setDescription("Description");
    private static final CategoryDto CATEGORY_DTO = new CategoryDto()
            .setName("Horror")
            .setDescription("Description");
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = -10L;
    private static final List<Category> CATEGORIES = List.of(CATEGORY_MODEL);
    private static final Pageable PAGEABLE = PageRequest.of(0, 10);
    private static final Page<Category> CATEGORY_PAGE
            = new PageImpl<>(CATEGORIES, PAGEABLE, CATEGORIES.size());

    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("Verify save() method works")
    public void save_validCategoryRequestDto_ReturnsCategoryDto() {
        when(categoryMapper.toModel(CATEGORY_REQUEST_DTO)).thenReturn(CATEGORY_MODEL);
        when(categoryRepository.save(CATEGORY_MODEL)).thenReturn(CATEGORY_MODEL);
        when(categoryMapper.toDto(CATEGORY_MODEL)).thenReturn(CATEGORY_DTO);

        CategoryDto savedCategoryDto = categoryService.save(CATEGORY_REQUEST_DTO);

        assertEquals(savedCategoryDto, CATEGORY_DTO);
    }

    @Test
    @DisplayName("Verify getById() method works")
    public void getById_validId_ReturnsCategoryDto() {
        when(categoryRepository.findById(VALID_ID)).thenReturn(Optional.of(CATEGORY_MODEL));
        when(categoryMapper.toDto(CATEGORY_MODEL)).thenReturn(CATEGORY_DTO);

        CategoryDto categoryDtoFromSearch = categoryService.getById(VALID_ID);

        assertEquals(categoryDtoFromSearch, CATEGORY_DTO);
    }

    @Test
    @DisplayName("Verify getById() method doesn't works with invalid id")
    public void getById_inValidId_ThrowsException() {
        when(categoryRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(INVALID_ID));

        String expected = "Category by provided " + INVALID_ID + " doesn't exists";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify findAll() method works")
    public void findAll_findAllPageableRequest_ReturnsCategoryDtoList() {
        when(categoryRepository.findAll(PAGEABLE)).thenReturn(CATEGORY_PAGE);
        when(categoryMapper.toDto(CATEGORY_MODEL)).thenReturn(CATEGORY_DTO);

        List<CategoryDto> categoryDtoList = categoryService.findAll(PAGEABLE);

        assertEquals(categoryDtoList.size(), CATEGORIES.size());
    }

    @Test
    @DisplayName("Verify update() method works")
    public void update_ValidCategoryRequestDto_ReturnsCategoryDto() {
        when(categoryMapper.toModel(CATEGORY_REQUEST_DTO)).thenReturn(CATEGORY_MODEL);
        when(categoryRepository.save(CATEGORY_MODEL)).thenReturn(CATEGORY_MODEL);
        when(categoryMapper.toDto(CATEGORY_MODEL)).thenReturn(CATEGORY_DTO);

        CategoryDto updatedCategoryDto = categoryService.update(VALID_ID, CATEGORY_REQUEST_DTO);

        assertEquals(updatedCategoryDto, CATEGORY_DTO);
    }
}
