package mate.academy.onlinebookstore.util;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.onlinebookstore.dto.BookDto;
import mate.academy.onlinebookstore.dto.CategoryRequestDto;
import mate.academy.onlinebookstore.dto.CategoryResponseDto;
import mate.academy.onlinebookstore.dto.CreateBookRequestDto;
import mate.academy.onlinebookstore.entity.Book;
import mate.academy.onlinebookstore.entity.Category;

public class TestUtils {
    public static final Long FIRST_MOCK_ID = 1L;

    public static final Long SECOND_MOCK_ID = 2L;

    public static Category createFirstCategory() {
        return new Category()
                .setId(FIRST_MOCK_ID)
                .setName("Category1")
                .setDescription("Description 1");
    }

    public static Category createSecondCategory() {
        return new Category()
                .setId(SECOND_MOCK_ID)
                .setName("Category2")
                .setDescription("Description 2");
    }

    public static CategoryResponseDto categoryToResponseDto(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription());
    }

    public static CategoryRequestDto createCategoryRequestDto() {
        return new CategoryRequestDto(
                "Category1",
                "Category1 description");
    }

    public static CreateBookRequestDto createRequestDto(List<Long> categoryIds) {
        return new CreateBookRequestDto(
                "First book",
                "First author",
                "123-456-789-0",
                14.17,
                "First book description",
                "imageBook1.jpg",
                new HashSet<>(categoryIds));
    }

    public static BookDto createBookDto(CreateBookRequestDto requestDto) {
        Book book = new Book();

        if (requestDto.title() != null) {
            book.setTitle(requestDto.title());
        }
        if (requestDto.author() != null) {
            book.setAuthor(requestDto.author());
        }
        if (requestDto.isbn() != null) {
            book.setIsbn(requestDto.isbn());
        }
        if (requestDto.price() != null) {
            book.setPrice(BigDecimal.valueOf(requestDto.price()));
        }
        if (requestDto.description() != null) {
            book.setDescription(requestDto.description());
        }
        if (requestDto.coverImage() != null) {
            book.setCoverImage(requestDto.coverImage());
        }

        return createBookDto(book);
    }

    public static BookDto createBookDto(Book book) {
        Set<Long> categoryIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPrice(),
                book.getDescription(),
                book.getCoverImage(),
                categoryIds);
    }

    public static Book createFirstBook(Set<Category> categories) {
        Book book = new Book();
        book.setId(FIRST_MOCK_ID);
        book.setTitle("Book 1");
        book.setAuthor("Author 1");
        book.setIsbn("123-456-789-1");
        book.setPrice(BigDecimal.valueOf(19));
        book.setCategories(categories);
        book.setDescription("Description for Book 1");
        book.setCoverImage("image1.jpg");
        return book;
    }

    public static Book createSecondBook(Set<Category> categories) {
        Book book = new Book();
        book.setId(SECOND_MOCK_ID);
        book.setTitle("Book 2");
        book.setAuthor("Author 2");
        book.setIsbn("123-456-789-2");
        book.setPrice(BigDecimal.valueOf(39));
        book.setCategories(categories);
        book.setDescription("Description for Book 2");
        book.setCoverImage("image2.jpg");
        return book;
    }

    public static Book createThirdBook(Set<Category> categories) {
        Book book = new Book();
        book.setId(3L);
        book.setTitle("Book 3");
        book.setAuthor("Author 2");
        book.setIsbn("123-456-789-3");
        book.setPrice(BigDecimal.valueOf(99));
        book.setCategories(categories);
        book.setDescription("Description for Book 3");
        book.setCoverImage("image3.jpg");
        return book;
    }
}
