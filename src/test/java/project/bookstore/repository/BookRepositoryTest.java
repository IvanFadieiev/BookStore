package project.bookstore.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.bookstore.model.Book;
import project.bookstore.model.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    private static final Book book = new Book()
            .setTitle("Harry Potter")
            .setAuthor("J.K.Rolling")
            .setIsbn("0-2936-9647-0")
            .setPrice(BigDecimal.valueOf(1000))
            .setCategories(Set.of(new Category(1L)));
    private static final Long CATEGORY_ID = 1L;
    private static final int INDEX = 1;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify method findAllByCategoryId() works")
    public void findAllByCategoryId_ValidCategoryId_ReturnsBookList() {
        bookRepository.save(book);
        List<Book> books = bookRepository.findAllByCategoryId(CATEGORY_ID);
        Book bookFromDb = books.get(INDEX);
        assertThat(bookFromDb).isEqualTo(book);
    }
}
