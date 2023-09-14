package project.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.bookstore.dto.book.BookDto;
import project.bookstore.model.Book;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("FROM Book b LEFT JOIN FETCH b.categories WHERE b.id = :categoryId")
    List<BookDto> findAllByCategoryId(Long categoryId);
}
