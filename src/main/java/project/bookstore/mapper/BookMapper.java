package project.bookstore.mapper;

import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.bookstore.config.MapperConfiguration;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.book.BookRequestDto;
import project.bookstore.model.Book;
import project.bookstore.model.Category;

@Mapper(config = MapperConfiguration.class)
public interface BookMapper {
    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Mapping(target = "categoriesId", ignore = true)
    BookDto toDto(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoriesId(book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }
    }

    @Mapping(target = "categories", ignore = true)
    Book toModel(BookRequestDto bookDto);

    @AfterMapping
    default void setCategories(@MappingTarget Book book, BookRequestDto requestDto) {
        if (requestDto.getCategoriesIds() != null) {
            book.setCategories(requestDto.getCategoriesIds().stream()
                    .map(Category::new)
                    .collect(Collectors.toSet()));
        }
    }
}
