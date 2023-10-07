package mate.academy.onlinebookstore.repository;

import java.util.List;
import mate.academy.onlinebookstore.entity.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
