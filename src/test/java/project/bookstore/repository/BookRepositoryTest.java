package project.bookstore.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import project.bookstore.model.Book;
import project.bookstore.model.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    private static final Book BOOK = new Book()
            .setTitle("Meditations")
            .setAuthor("Marcus Aurelius")
            .setIsbn("978-4-2596-7831-9")
            .setPrice(BigDecimal.valueOf(1000))
            .setDescription("Description")
            .setCoverImage("Cover image")
            .setCategories(Set.of(new Category(1L)));

    private static final Long CATEGORY_ID = 1L;
    private static final int FIRST_ELEMENT = 0;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify findAllByCategoryId() method works")
    @Sql(scripts = {
            "classpath:database/scripts/books/clear-books-table.sql",
            "classpath:database/scripts/category/clear-categories-table.sql",
            "classpath:database/scripts/category/clear-book-categories-table.sql",
            "classpath:database/scripts/category/add-category-to-categories-table.sql",
            "classpath:database/scripts/category/add-book-category-to-book-categories-table.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/books/clear-books-table.sql",
            "classpath:database/scripts/category/clear-categories-table.sql",
            "classpath:database/scripts/category/clear-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ValidCategoryId_ReturnsBookList() {
        bookRepository.save(BOOK);
        List<Book> books = bookRepository.findAllByCategoryId(CATEGORY_ID);
        Book bookFromDb = books.get(FIRST_ELEMENT);
        EqualsBuilder.reflectionEquals(bookFromDb, BOOK, "id");
    }
}
