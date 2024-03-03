package mate.academy.onlinebookstore.repository.book;

import java.util.List;
import mate.academy.onlinebookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("FROM Book b LEFT JOIN FETCH b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(Long categoryId);
}
