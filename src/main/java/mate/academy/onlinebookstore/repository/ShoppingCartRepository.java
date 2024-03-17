package mate.academy.onlinebookstore.repository;

import mate.academy.onlinebookstore.entity.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"user"})//, "cartItems", "cartItems.book"
    ShoppingCart findByUserEmail(String userEmail);
}
