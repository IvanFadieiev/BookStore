package project.bookstore.repository;

import java.util.List;
import project.bookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    Book getBookById(Long id);

    List<Book> findAll();
}
