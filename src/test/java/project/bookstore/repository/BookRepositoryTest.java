package project.bookstore.repository;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
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
    private static final String CLEAR_BOOKS_TABLE
            = "classpath:database/scripts/books/clear-books-table.sql";
    private static final String CLEAR_CATEGORIES_TABLE
            = "classpath:database/scripts/category/clear-categories-table.sql";
    private static final String CLEAR_BOOK_CATEGORIES_TABLE
            = "classpath:database/scripts/category/clear-book-categories-table.sql";
    private static final String ADD_CATEGORY
            = "classpath:database/scripts/category/add-category-to-categories-table.sql";
    private static final String ADD_BOOK_CATEGORY
            = "classpath:database/scripts/category/add-book-category-to-book-categories-table.sql";

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify findAllByCategoryId() method works")
    @Sql(scripts = {
            CLEAR_BOOKS_TABLE, CLEAR_CATEGORIES_TABLE,
            CLEAR_BOOK_CATEGORIES_TABLE, ADD_CATEGORY,
            ADD_BOOK_CATEGORY
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_BOOKS_TABLE, CLEAR_CATEGORIES_TABLE,
            CLEAR_BOOK_CATEGORIES_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ValidCategoryId_ReturnsBookList() {
        bookRepository.save(BOOK);
        List<Book> books = bookRepository.findAllByCategoryId(CATEGORY_ID);
        Book bookFromDb = books.get(FIRST_ELEMENT);
        reflectionEquals(bookFromDb, BOOK, "id");
    }
}
