package project.bookstore.mapper;

import org.mapstruct.Mapper;
import project.bookstore.config.MapperConfiguration;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CategoryRequestDto;
import project.bookstore.model.Category;

@Mapper(config = MapperConfiguration.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CategoryRequestDto categoryRequestDto);
}
