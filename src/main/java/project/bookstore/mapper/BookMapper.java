package project.bookstore.mapper;

import org.mapstruct.Mapper;
import project.bookstore.config.MapperConfiguration;
import project.bookstore.dto.BookDto;
import project.bookstore.dto.BookRequestDto;
import project.bookstore.model.Book;

@Mapper(config = MapperConfiguration.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(BookRequestDto bookDto);
}
