package mate.academy.onlinebookstore.repository;

import mate.academy.onlinebookstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
