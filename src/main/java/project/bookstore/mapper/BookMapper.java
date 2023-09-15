package project.bookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.bookstore.config.MapperConfiguration;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookRequestDto;
import project.bookstore.model.Book;

@Mapper(config = MapperConfiguration.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    Book toModel(BookRequestDto bookDto);
}
