package project.bookstore.service;

import java.util.List;
import project.bookstore.dto.BookDto;
import project.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto create(CreateBookRequestDto book);

    BookDto findById(Long id);

    List<BookDto> findAll();
}
