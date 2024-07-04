package mate.academy.onlinebookstore.service;

import static mate.academy.onlinebookstore.util.UtilsForTests.FIRST_MOCK_ID;
import static mate.academy.onlinebookstore.util.UtilsForTests.createFirstBook;
import static mate.academy.onlinebookstore.util.UtilsForTests.createSecondBook;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.onlinebookstore.dto.BookDto;
import mate.academy.onlinebookstore.dto.CreateBookRequestDto;
import mate.academy.onlinebookstore.entity.Book;
import mate.academy.onlinebookstore.entity.Category;
import mate.academy.onlinebookstore.mapper.BookMapper;
import mate.academy.onlinebookstore.repository.book.BookRepository;
import mate.academy.onlinebookstore.service.impl.BookServiceImpl;
import mate.academy.onlinebookstore.util.UtilsForTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("""
        Test findAll() method. It returns list of all book
            """)
    void findAll_validPageable_returnsAllBooks() {
        //given
        Category category1 = UtilsForTests.createFirstCategory();
        Book firstBook = createFirstBook(Set.of(category1));
        BookDto firstBookDto = UtilsForTests.createBookDto(firstBook);
        Book secondBook = createSecondBook(Set.of(category1));
        BookDto secondBookDto = UtilsForTests.createBookDto(secondBook);
        List<Book> expected = List.of(firstBook, secondBook);
        List<BookDto> expectedDto = List.of(firstBookDto, secondBookDto);
        Pageable pageable = PageRequest.of(0, 5);
        when(bookRepository.findAll(pageable)).thenReturn(new PageImpl<>(expected));
        when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        //when
        List<BookDto> actual = bookService.findAll(pageable);

        //then
        assertEquals(expectedDto.get(0), actual.get(0));
    }

    @Test
    @DisplayName("""
        Test findById() method. It returns one book
            """)
    void findById_validId_returnsOneBook() {
        //given
        Category category1 = UtilsForTests.createFirstCategory();
        Book expected = createFirstBook(Set.of(category1));
        BookDto expectedDto = UtilsForTests.createBookDto(expected);
        when(bookRepository.findById(FIRST_MOCK_ID)).thenReturn(Optional.of(expected));
        when(bookMapper.toDto(expected)).thenReturn(expectedDto);
        //when
        BookDto actual = bookService.findById(FIRST_MOCK_ID);

        //then
        assertEquals(expectedDto.id(), actual.id());
    }

    @Test
    @DisplayName("""
        Test update() method. It updates one book
            """)
    public void update_validId_returnsUpdateOneBook() {
        //given
        Category categoryFirst = UtilsForTests.createFirstCategory();
        CreateBookRequestDto createBookRequestDto =
                       UtilsForTests.createRequestDto(List.of(categoryFirst.getId()));
        Book editedBook = UtilsForTests.createFirstBook(Set.of(categoryFirst));
        editedBook.setPrice(BigDecimal.valueOf(createBookRequestDto.price()));
        BookDto editedBookDto = UtilsForTests.createBookDto(editedBook);

        when(bookRepository.findById(FIRST_MOCK_ID)).thenReturn(Optional.of(editedBook));
        when(bookRepository.save(editedBook)).thenReturn(editedBook);
        when(bookMapper.toDto(editedBook)).thenReturn(editedBookDto);

        //when
        BookDto actual = bookService.update(FIRST_MOCK_ID, createBookRequestDto);

        //then
        assertEquals(editedBookDto, actual);
    }

    @Test
    @DisplayName("""
        Test deleteById() method. It deletes one book by id
            """)
    public void deleteById_validId_ok() {
        //given
        doNothing().when(bookRepository).deleteById(UtilsForTests.FIRST_MOCK_ID);

        //when
        bookService.deleteById(UtilsForTests.FIRST_MOCK_ID);

        //then
        verify(bookRepository, times(1)).deleteById(UtilsForTests.FIRST_MOCK_ID);
    }

    @Test
    @DisplayName("""
        Test save(). It tests create new book, add book to database and return bookDto
            """)
    public void save_checkValidData_createOneBook() {
        //given
        Category category1 = UtilsForTests.createFirstCategory();
        Book createBook = createFirstBook(Set.of(category1));
        List<Book> bookList = Collections.singletonList(createBook);
        Pageable pageable = PageRequest.of(0, 5);
        Page<Book> bookPage = new PageImpl<>(bookList, pageable, bookList.size());
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        //when
        List<BookDto> bookDtos = bookService.findAll(pageable);
        //then
        assertThat(bookDtos, hasSize(1));
    }

    @Test
    void deleteProductWithExceptionTest() {
        doThrow(RuntimeException.class).when(bookRepository).deleteById(any());

        assertThrows(RuntimeException.class,
                () -> bookService.deleteById(UtilsForTests.SECOND_MOCK_ID));
    }

}
