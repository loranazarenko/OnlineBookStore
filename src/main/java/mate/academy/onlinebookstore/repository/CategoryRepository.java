package mate.academy.onlinebookstore.repository;

import mate.academy.onlinebookstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
