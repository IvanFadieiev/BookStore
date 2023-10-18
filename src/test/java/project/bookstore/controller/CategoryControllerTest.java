package project.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import project.bookstore.dto.book.BookDto;
import project.bookstore.dto.category.CategoryDto;
import project.bookstore.dto.category.CategoryRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
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
            "classpath:database/scripts/category/add-category-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/category/remove-category-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryById_ValidId_ReturnsExpectedCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        CategoryDto expectedCategoryDto = objectMapper.readValue(result
                .getResponse().getContentAsString(), CategoryDto.class);

        EqualsBuilder.reflectionEquals(expectedCategoryDto, CATEGORY_DTO, "id");
    }

    @WithMockUser(username = "alice", roles = {"USER"})
    @Test
    @DisplayName("Verify getAll() method works")
    @Sql(scripts = {
            "classpath:database/scripts/category/add-three-categories-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/category/clear-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAll_RequestToGetAllCategories_ReturnsCategoryDtoList() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        CategoryDto[] categoryDtoArray = objectMapper.readValue(result
                .getResponse()
                .getContentAsByteArray(), CategoryDto[].class);

        Assertions.assertNotNull(categoryDtoArray);
        Assertions.assertEquals(EXPECTED_LIST_SIZE, categoryDtoArray.length);
    }

    @WithMockUser(username = "alice", roles = {"USER"})
    @Test
    @DisplayName("Verify getBooksByCategoryId() method works")
    @Sql(scripts = {
            "classpath:database/scripts/books/clear-books-table.sql",
            "classpath:database/scripts/category/clear-book-categories-table.sql",
            "classpath:database/scripts/books/add-book-to-books-table.sql",
            "classpath:database/scripts/category/add-category-to-categories-table.sql",
            "classpath:database/scripts/category/add-book-category-to-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/books/clear-books-table.sql",
            "classpath:database/scripts/category/clear-categories-table.sql",
            "classpath:database/scripts/category/clear-book-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBooksByCategoryId_RequestToGetBookDtoList_ReturnsBookDtoList() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/" + VALID_ID + "/books")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        BookDto[] expectedBookDto = objectMapper.readValue(result
                .getResponse()
                .getContentAsByteArray(), BookDto[].class);

        Assertions.assertNotNull(expectedBookDto);
        Assertions.assertEquals(expectedBookDto.length, EXPECTED_BOOK_DTO_LIST_SIZE);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify create() method works")
    @Sql(scripts = {
            "classpath:database/scripts/category/add-category-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/category/remove-category-from-categories-table.sql"
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

        Assertions.assertNotNull(expectedDto);
        Assertions.assertNotNull(expectedDto.getId());
        EqualsBuilder.reflectionEquals(expectedDto, CATEGORY_DTO, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify delete() method works")
    @Sql(scripts = {
            "classpath:database/scripts/category/add-category-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/category/remove-category-from-categories-table.sql"
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

        Assertions.assertNotNull(updatedDto);
        Assertions.assertEquals(updatedDto.getDescription(), UPDATED_DESCRIPTION);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Verify delete() method works")
    @Sql(scripts = {
            "classpath:database/scripts/category/add-category-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/scripts/category/remove-category-from-categories-table.sql"
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
