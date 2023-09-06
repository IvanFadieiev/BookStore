package project.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
