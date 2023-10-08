package mate.academy.onlinebookstore.repository;

import java.util.List;
import mate.academy.onlinebookstore.entity.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
