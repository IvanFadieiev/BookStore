package project.bookstore;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import project.bookstore.model.Book;
import project.bookstore.repository.BookRepository;

@SpringBootApplication
public class BookStoreApplication {
    private final BookRepository bookRepository;

    @Autowired
    public BookStoreApplication(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Lord of the Rings");
            book.setAuthor("Tolkien J.R.");
            book.setIsbn("123456");
            book.setPrice(BigDecimal.valueOf(1000));
            bookRepository.save(book);
            bookRepository.findAll().forEach(System.out::println);
        };
    }
}
