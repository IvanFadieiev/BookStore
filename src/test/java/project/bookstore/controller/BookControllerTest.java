package project.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
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
import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.book.BookRequestDto;

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
    private static final String REMOVE_BOOK
            = "classpath:database/scripts/books/remove-book-from-books-table.sql";
    private static final String ADD_BOOK
            = "classpath:database/scripts/books/add-book-to-books-table.sql";
    private static final String ADD_THREE_BOOK
            = "classpath:database/scripts/books/add-three-books-to-books-table.sql";
    private static final String CLEAR_BOOKS_TABLE
            = "classpath:database/scripts/books/clear-books-table.sql";
    private static final String CLEAR_CATEGORIES_TABLE
            = "classpath:database/scripts/category/clear-categories-table.sql";
    private static final String ADD_CATEGORY
            = "classpath:database/scripts/category/add-category-to-categories-table.sql";
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
            REMOVE_BOOK
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
        assertNotNull(actual);
        assertNotNull(actual.getId());
        reflectionEquals(actual, BOOK_REQUEST_DTO, "id");
    }

    @WithMockUser(username = "alice", roles = {"USER"})
    @Test
    @DisplayName("Verify getBookById() method works")
    @Sql(scripts = {
            ADD_BOOK
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            REMOVE_BOOK
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookById_ValidId_ReturnsExpectedBook() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        BookDto bookDtoFromDB = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        assertNotNull(bookDtoFromDB);
        reflectionEquals(BOOK_REQUEST_DTO, bookDtoFromDB,"id");
    }

    @WithMockUser(username = "alice", roles = {"USER"})
    @Test
    @DisplayName("Verify getAll() method works")
    @Sql(scripts = {
            ADD_THREE_BOOK
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_BOOKS_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAll_getAllBooksRequest_ReturnBookDtoList() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        BookDto[] bookDtoArray = objectMapper.readValue(result
                .getResponse()
                .getContentAsByteArray(), BookDto[].class);
        assertNotNull(bookDtoArray);
        assertEquals(EXPECTED_LENGTH, bookDtoArray.length);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify update() method works")
    @Sql(scripts = {
            CLEAR_CATEGORIES_TABLE, ADD_BOOK,
            ADD_CATEGORY
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            REMOVE_BOOK
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

        assertNotNull(updatedBookDto);
        assertEquals(UPDATED_PRICE, updatedBookDto.getPrice());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify delete() method works")
    @Sql(scripts = {
            ADD_BOOK
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            REMOVE_BOOK
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
