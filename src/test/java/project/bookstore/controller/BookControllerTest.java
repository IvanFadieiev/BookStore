package project.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final BookRequestDto BOOK_REQUEST_DTO = new BookRequestDto()
            .setTitle("Meditations")
            .setAuthor("Marcus Aurelius")
            .setIsbn("978-4-2596-7831-9")
            .setPrice(BigDecimal.valueOf(1000))
            .setDescription("Description")
            .setCoverImage("Cover image")
            .setCategoriesIds(Set.of(1L));
    private static final BookRequestDto BOOK_UPDATE_REQUEST_DTO = new BookRequestDto()
            .setTitle("Meditations")
            .setAuthor("Marcus Aurelius")
            .setIsbn("978-4-2596-7831-9")
            .setPrice(BigDecimal.valueOf(1100))
            .setDescription("Description")
            .setCoverImage("Cover image")
            .setCategoriesIds(Set.of(1L));
    private static final Long VALID_ID = 1L;
    private static final int EXPECTED_LENGTH = 3;
    private static final BigDecimal UPDATED_PRICE = BigDecimal.valueOf(1100);
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify createBook() method works")
    @Sql(scripts = {
            "classpath:database/scripts/books/remove-book-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createBook_ValidRequestDto_ReturnsBookDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(BOOK_REQUEST_DTO);
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(actual, BOOK_REQUEST_DTO, "id");
    }

    @WithMockUser(username = "alice", roles = {"USER"})
    @Test
    @DisplayName("Verify getBookById() method works")
    @Sql(scripts = {
            "classpath:database/scripts/books/add-book-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/books/remove-book-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookById_ValidId_ReturnsExpectedBook() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        BookDto bookDtoFromDB = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        Assertions.assertNotNull(bookDtoFromDB);
        EqualsBuilder.reflectionEquals(BOOK_REQUEST_DTO, bookDtoFromDB,"id");
    }

    @WithMockUser(username = "alice", roles = {"USER"})
    @Test
    @DisplayName("Verify getAll() method works")
    @Sql(scripts = {
            "classpath:database/scripts/books/add-three-books-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/books/clear-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAll_getAllBooksRequest_ReturnBookDtoList() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        BookDto[] bookDtoArray = objectMapper.readValue(result
                .getResponse()
                .getContentAsByteArray(), BookDto[].class);
        Assertions.assertNotNull(bookDtoArray);
        Assertions.assertEquals(EXPECTED_LENGTH, bookDtoArray.length);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify update() method works")
    @Sql(scripts = {
            "classpath:database/scripts/category/clear-categories-table.sql",
            "classpath:database/scripts/books/add-book-to-books-table.sql",
            "classpath:database/scripts/category/add-category-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/books/remove-book-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void update_updateExistingBook_ReturnUpdatedBookDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(BOOK_UPDATE_REQUEST_DTO);
        MvcResult result = mockMvc.perform(put("/books/" + VALID_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isAccepted())
                .andReturn();

        BookDto updatedBookDto = objectMapper.readValue(result
                .getResponse().getContentAsString(), BookDto.class);

        Assertions.assertNotNull(updatedBookDto);
        Assertions.assertEquals(UPDATED_PRICE, updatedBookDto.getPrice());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify delete() method works")
    @Sql(scripts = {
            "classpath:database/scripts/books/add-book-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/books/remove-book-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_deleteExistingBook_ReturnStatusAccepted() throws Exception {
        mockMvc.perform(delete("/books/" + VALID_ID))
                        .andExpect(status().isAccepted())
                        .andReturn();
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                        new ClassPathResource("database/scripts/books/clear-books-table.sql")
            );
        }
    }
}
