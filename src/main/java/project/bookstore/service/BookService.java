package project.bookstore.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookDtoWithoutCategoryIds;
import project.bookstore.dto.book.BookRequestDto;

public interface BookService {
    BookDto create(BookRequestDto book);

    BookDto findById(Long id);

    BookDto update(Long id, BookRequestDto book);

    void deleteById(Long id);

    List<BookDto> findAll(Pageable pageable);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id);
}
