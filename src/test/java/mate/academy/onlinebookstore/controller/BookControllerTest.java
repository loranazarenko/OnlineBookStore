package mate.academy.onlinebookstore.controller;

import static mate.academy.onlinebookstore.util.UtilsForTests.createFirstBook;
import static mate.academy.onlinebookstore.util.UtilsForTests.createSecondBook;
import static mate.academy.onlinebookstore.util.UtilsForTests.createThirdBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.onlinebookstore.dto.BookDto;
import mate.academy.onlinebookstore.dto.CreateBookRequestDto;
import mate.academy.onlinebookstore.entity.Book;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
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
    @DisplayName("""
        Check getAll(). Check if return list of all books from database
            """)
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getAll_checkRequest_returnPageWithBooks() throws Exception {
        //Given
        Category category1 = UtilsForTests.createFirstCategory();
        Category category2 = UtilsForTests.createSecondCategory();
        BookDto firstBookDto = UtilsForTests.createBookDto(createFirstBook(Set.of(category1)));
        BookDto secondBookDto = UtilsForTests.createBookDto(createSecondBook(Set.of(category2)));
        BookDto thirdBookDto = UtilsForTests.createBookDto(createThirdBook(Set.of(category2)));
        List<BookDto> expected = List.of(firstBookDto, secondBookDto, thirdBookDto);
        Pageable pageable = PageRequest.of(0, 5);

        //When
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        //Then
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
    }

    @Test
    @DisplayName("""
        Check getBookById(). Check if return a one book by id from database
            """)
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getBookById_checkValidId_returnOneBook() throws Exception {
        //Given
        Category category = UtilsForTests.createFirstCategory();
        Book book = createFirstBook(Set.of(category));
        BookDto expected = UtilsForTests.createBookDto(book);

        //When
        MvcResult result = mockMvc.perform(get("/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);

        //Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
        Check wrong getBookById(). Check throw a exception
            """)
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getBookById_checkInvalidId_returnOneBook() throws Exception {
        //Given
        Long invalidId = 9L;
        String message = "Can't find book by id9";
        //When Then
        ResultActions resultActions = mockMvc.perform(get("/books/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value(message));
    }

    @Test
    @DisplayName("""
                 Check createBook(). Check if create new book, "
                 + "add book to database and return bookDto
                 """)
    @WithMockUser(roles = {"ADMIN"})
    public void createBook_checkValidData_createOneBook() throws Exception {
        //Given
        CreateBookRequestDto requestDto = UtilsForTests.createRequestDto(
                List.of(UtilsForTests.createFirstCategory().getId()));
        BookDto expected = UtilsForTests.createBookDto(requestDto);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                        post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        //Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("""
        Check updateBook(). Check if update data some book
            """)
    @WithMockUser(roles = {"USER", "ADMIN"})
    void updateBook_checkValidId_updatedOneBook() throws Exception {
        //Given
        Long updatedBookId = 1L;
        CreateBookRequestDto requestDto =
                UtilsForTests.createRequestDto(List.of(
                        UtilsForTests.createFirstCategory().getId()));

        BookDto updated = UtilsForTests.createBookDto(requestDto);

        BookDto expected = new BookDto(
                updatedBookId,
                updated.title(),
                updated.author(),
                updated.isbn(),
                updated.price(),
                updated.description(),
                updated.coverImage(),
                updated.categoryIds()
        );

        //When
        MvcResult result = mockMvc.perform(put("/books/{id}", updatedBookId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertEquals(expected.id(), actual.id());
    }

    @Test
    @DisplayName("""
        Check delete(). Check if delete some book
            """)
    @WithMockUser(roles = {"ADMIN"})
    void delete_validId_ok() throws Exception {
        Book deleted = UtilsForTests.createFirstBook(Set.of(UtilsForTests.createFirstCategory()));
        mockMvc.perform(delete("/books/{id}", deleted.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
