package mate.academy.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.onlinebookstore.dto.BookDto;
import mate.academy.onlinebookstore.dto.CategoryRequestDto;
import mate.academy.onlinebookstore.dto.CategoryResponseDto;
import mate.academy.onlinebookstore.entity.Category;
import mate.academy.onlinebookstore.util.UtilsForTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void beforeAll() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        teardown();
    }

    @BeforeEach
    public void beforeEach(
            @Autowired DataSource dataSource
    ) {
        upData();
    }

    @AfterEach
    public void afterEach(
            @Autowired DataSource dataSource
    ) {
        teardown();
    }

    @SneakyThrows
    private void upData() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/insert-category-to-categories_table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/insert-three-books-to-books_table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/insert-category-in-books.sql")
            );
        }
    }

    @SneakyThrows
    private void teardown() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/"
                            + "remove-ids-from-books_categories_table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/remove-all-from-books_table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/remove-all-from-categories_table.sql"));
        }
    }

    @Test
    @DisplayName("Check getAll(). "
                 + "Check if return list of all categories from database")
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getAll_CheckRequest_ReturnPageWithAllCategories() throws Exception {
        //Given
        Category categoryFirst = UtilsForTests.createFirstCategory();
        Category categorySecond = UtilsForTests.createSecondCategory();
        CategoryResponseDto categoryResponseDtoFirst =
                UtilsForTests.categoryToResponseDto(categoryFirst);
        CategoryResponseDto categoryResponseDtoSecond =
                UtilsForTests.categoryToResponseDto(categorySecond);
        List<CategoryResponseDto> expected =
                List.of(categoryResponseDtoFirst, categoryResponseDtoSecond);
        Pageable pageable = PageRequest.of(0, 5);

        //When
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryResponseDto> actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>() {
                        });

        //Then
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    @DisplayName("Check getCategoryById(). Check if return a one category by id from database")
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getCategoryById_CheckValidId_ReturnOneCategory() throws Exception {
        //Given
        Category category = UtilsForTests.createFirstCategory();
        CategoryResponseDto expected = UtilsForTests.categoryToResponseDto(category);

        //When
        MvcResult result = mockMvc.perform(get("/categories/{id}",
                        category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryResponseDto.class);

        //Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Check wrong getCategoryById(). Check throw a exception")
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getCategoryById_CheckInvalidId_ReturnOneCategory() throws Exception {
        //Given
        Long invalidId = 9L;
        String message = "Can't find category by id9";
        //When Then
        mockMvc.perform(get("/categories/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(message));
    }

    @Test
    @DisplayName("Check createCategory(). Check if create new category, "
                 + "add category to database and return CategoryResponseDto")
    @WithMockUser(roles = {"ADMIN"})
    public void createCategory_CheckValidData_CreateOneCategory() throws Exception {
        //Given
        CategoryRequestDto categoryRequestDto = UtilsForTests.createCategoryRequestDto();
        Category categoryCreated = new Category()
                .setName(categoryRequestDto.name())
                .setDescription(categoryRequestDto.description());

        CategoryResponseDto expected = UtilsForTests.categoryToResponseDto(categoryCreated);

        //When
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(categoryRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Check updateCategory(). Check if update data some category")
    @WithMockUser(roles = {"ADMIN"})
    void updateCategory_CheckValidId_UpdatedOneCategory() throws Exception {
        //Given
        Long updatedCategoryId = 1L;
        CategoryRequestDto categoryRequestDto =
                UtilsForTests.createCategoryRequestDto();
        Category updated = new Category()
                .setName(categoryRequestDto.name())
                .setDescription(categoryRequestDto.description());
        CategoryResponseDto updatedDto = UtilsForTests.categoryToResponseDto(updated);
        CategoryResponseDto expected = new CategoryResponseDto(
                updatedCategoryId,
                updatedDto.name(),
                updatedDto.description()
        );

        //When
        MvcResult result = mockMvc.perform(put("/categories/{id}",
                        updatedCategoryId)
                        .content(objectMapper.writeValueAsString(expected))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        CategoryResponseDto actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryResponseDto.class);
        assertEquals(expected.id(), actual.id());
    }

    @Test
    @DisplayName("Check delete(). Check if delete some category")
    @WithMockUser(roles = {"ADMIN"})
    void delete_ValidId_Ok() throws Exception {
        Category deleted = UtilsForTests.createFirstCategory();
        mockMvc.perform(delete("/categories/{id}", deleted.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
