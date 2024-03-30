package mate.academy.onlinebookstore.service;

import java.util.List;
import mate.academy.onlinebookstore.dto.OrderItemResponseDto;
import mate.academy.onlinebookstore.dto.OrderRequestDto;
import mate.academy.onlinebookstore.dto.OrderResponseDto;
import mate.academy.onlinebookstore.dto.OrderUpdateRequestDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    List<OrderResponseDto> getAllOrdersByUser(Pageable pageable, Long userId);

    OrderResponseDto saveOrder(OrderRequestDto orderRequestDto, Long userId);

    void updateOrderStatus(Long id, OrderUpdateRequestDto orderPatchRequestDto);

    List<OrderItemResponseDto> getAllItemsByOrder(Long orderId);

    OrderItemResponseDto getItemByOrder(Long orderId, Long itemId);
}
