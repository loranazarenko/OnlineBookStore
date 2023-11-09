package mate.academy.onlinebookstore.service;

import java.util.List;
import mate.academy.onlinebookstore.dto.BookDto;
import mate.academy.onlinebookstore.dto.BookSearchParameters;
import mate.academy.onlinebookstore.dto.CreateBookRequestDto;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> findAll();

    BookDto update(Long id, CreateBookRequestDto bookDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters params);
}
