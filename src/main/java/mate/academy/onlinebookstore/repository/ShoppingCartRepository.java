package mate.academy.onlinebookstore.repository;

import java.util.Optional;
import mate.academy.onlinebookstore.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUserId(Long userId);
}
