package mate.academy.onlinebookstore.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.onlinebookstore.entity.Order;
import mate.academy.onlinebookstore.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser_Id(Pageable pageable, Long userId);

    @Query("SELECT oi FROM Order o LEFT JOIN o.orderItems oi "
            + "WHERE o.id = :orderId AND oi.id = :itemId")
    Optional<OrderItem> findOneOrderItem(Long orderId, Long itemId);
}
