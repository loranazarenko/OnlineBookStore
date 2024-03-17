package mate.academy.onlinebookstore.repository;

import java.util.Optional;
import mate.academy.onlinebookstore.entity.CartItem;
import mate.academy.onlinebookstore.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByShoppingCartAndBookId(ShoppingCart shoppingCart, Long id);

    Optional<CartItem> findByShoppingCartAndId(ShoppingCart shoppingCart, Long id);
}
