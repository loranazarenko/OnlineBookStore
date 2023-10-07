package mate.academy.onlinebookstore.service;

import java.util.List;
import mate.academy.onlinebookstore.entity.Book;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
