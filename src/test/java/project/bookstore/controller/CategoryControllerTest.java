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
import java.sql.Connection;
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
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CategoryRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final Long VALID_ID = 1L;
    private static final Long EXPECTED_LIST_SIZE = 3L;
    private static final Long EXPECTED_BOOK_DTO_LIST_SIZE = 1L;
    private static final String UPDATED_DESCRIPTION = "Updated description";

    private static final CategoryDto CATEGORY_DTO = new CategoryDto()
            .setName("Horror")
            .setDescription("Description");
    private static final CategoryRequestDto CATEGORY_REQUEST_DTO = new CategoryRequestDto()
            .setName("Horror")
            .setDescription("Description");
    private static final CategoryDto UPDATED_CATEGORY_DTO = new CategoryDto()
            .setName("Horror")
            .setDescription("Updated description");
    private static final String ADD_CATEGORY
            = "classpath:database/scripts/category/add-category-to-categories-table.sql";
    private static final String REMOVE_CATEGORY
            = "classpath:database/scripts/category/remove-category-from-categories-table.sql";
    private static final String ADD_THREE_CATEGORIES
            = "classpath:database/scripts/category/add-three-categories-to-categories-table.sql";
    private static final String CLEAR_CATEGORIES
            = "classpath:database/scripts/category/clear-categories-table.sql";
    private static final String CLEAR_BOOKS_TABLE
            = "classpath:database/scripts/books/clear-books-table.sql";
    private static final String CLEAR_BOOK_CATEGORIES_TABLE
            = "classpath:database/scripts/category/clear-book-categories-table.sql";
    private static final String ADD_BOOK
            = "classpath:database/scripts/books/add-book-to-books-table.sql";
    private static final String ADD_BOOK_CATEGORY
            = "classpath:database/scripts/category/add-book-category-to-book-categories-table.sql";

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

    @WithMockUser(username = "alice", roles = {"USER"})
    @Test
    @DisplayName("Verify getCategoryById() method works")
    @Sql(scripts = {
            ADD_CATEGORY
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            REMOVE_CATEGORY
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryById_ValidId_ReturnsExpectedCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        CategoryDto expectedCategoryDto = objectMapper.readValue(result
                .getResponse().getContentAsString(), CategoryDto.class);

        reflectionEquals(expectedCategoryDto, CATEGORY_DTO, "id");
    }

    @WithMockUser(username = "alice", roles = {"USER"})
    @Test
    @DisplayName("Verify getAll() method works")
    @Sql(scripts = {
            ADD_THREE_CATEGORIES
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_CATEGORIES
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAll_RequestToGetAllCategories_ReturnsCategoryDtoList() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        CategoryDto[] categoryDtoArray = objectMapper.readValue(result
                .getResponse()
                .getContentAsByteArray(), CategoryDto[].class);

        assertNotNull(categoryDtoArray);
        assertEquals(EXPECTED_LIST_SIZE, categoryDtoArray.length);
    }

    @WithMockUser(username = "alice", roles = {"USER"})
    @Test
    @DisplayName("Verify getBooksByCategoryId() method works")
    @Sql(scripts = {
            CLEAR_BOOKS_TABLE, CLEAR_BOOK_CATEGORIES_TABLE,
            ADD_BOOK, ADD_CATEGORY,ADD_BOOK_CATEGORY
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            CLEAR_BOOKS_TABLE, CLEAR_CATEGORIES, CLEAR_BOOK_CATEGORIES_TABLE
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBooksByCategoryId_RequestToGetBookDtoList_ReturnsBookDtoList() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/" + VALID_ID + "/books")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        BookDto[] expectedBookDto = objectMapper.readValue(result
                .getResponse()
                .getContentAsByteArray(), BookDto[].class);

        assertNotNull(expectedBookDto);
        assertEquals(expectedBookDto.length, EXPECTED_BOOK_DTO_LIST_SIZE);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify create() method works")
    @Sql(scripts = {
            ADD_CATEGORY
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            REMOVE_CATEGORY
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void create_validCategoryRequestDto_ReturnsCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(post("/categories")
                .content(objectMapper.writeValueAsString(CATEGORY_REQUEST_DTO))
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        CategoryDto expectedDto = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), CategoryDto.class);

        assertNotNull(expectedDto);
        assertNotNull(expectedDto.getId());
        reflectionEquals(expectedDto, CATEGORY_DTO, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify delete() method works")
    @Sql(scripts = {
            ADD_CATEGORY
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            REMOVE_CATEGORY
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void update_validRequestDto_ReturnsUpdatedCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(put("/categories/" + VALID_ID)
                        .content(objectMapper.writeValueAsString(UPDATED_CATEGORY_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        CategoryDto updatedDto = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), CategoryDto.class);

        assertNotNull(updatedDto);
        assertEquals(updatedDto.getDescription(), UPDATED_DESCRIPTION);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify delete() method works")
    @Sql(scripts = {
            ADD_CATEGORY
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            REMOVE_CATEGORY
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void delete_validId_ReturnsStatusAccepted() throws Exception {
        mockMvc.perform(delete("/categories/" + VALID_ID))
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
                    new ClassPathResource("database/scripts/category/clear-categories-table.sql")
            );
        }
    }
}
