package mate.academy.onlinebookstore.repository;

import mate.academy.onlinebookstore.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    Specification<Book> getSpecification(String[] params);
}
