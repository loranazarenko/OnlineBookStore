package mate.academy.onlinebookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import mate.academy.onlinebookstore.dto.OrderItemResponseDto;
import mate.academy.onlinebookstore.dto.OrderRequestDto;
import mate.academy.onlinebookstore.dto.OrderResponseDto;
import mate.academy.onlinebookstore.dto.OrderUpdateRequestDto;
import mate.academy.onlinebookstore.entity.CartItem;
import mate.academy.onlinebookstore.entity.Order;
import mate.academy.onlinebookstore.entity.OrderItem;
import mate.academy.onlinebookstore.entity.ShoppingCart;
import mate.academy.onlinebookstore.entity.User;
import mate.academy.onlinebookstore.exception.EntityNotFoundException;
import mate.academy.onlinebookstore.mapper.OrderItemMapper;
import mate.academy.onlinebookstore.mapper.OrderMapper;
import mate.academy.onlinebookstore.repository.OrderItemRepository;
import mate.academy.onlinebookstore.repository.OrderRepository;
import mate.academy.onlinebookstore.repository.ShoppingCartRepository;
import mate.academy.onlinebookstore.repository.user.UserRepository;
import mate.academy.onlinebookstore.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;

    @Override
    public List<OrderResponseDto> getAllOrdersByUser(Pageable pageable, Long userId) {
        return orderRepository.findAllByUser_Id(pageable, userId).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public OrderResponseDto saveOrder(OrderRequestDto orderRequestDto, Long userId) {
        Order order = orderMapper.toModel(orderRequestDto);
        createOrder(userId, order);
        return orderMapper.toDto(order);
    }

    @Transactional
    @Override
    public void updateOrderStatus(Long orderId, OrderUpdateRequestDto orderPatchRequestDto) {
        Order order = orderRepository.getReferenceById(orderId);
        order.setStatus(Order.Status.valueOf(orderPatchRequestDto.status().name()));
    }

    @Override
    public List<OrderItemResponseDto> getAllItemsByOrder(Long orderId) {
        Order order = orderRepository.getReferenceById(orderId);
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getItemByOrder(Long orderId, Long itemId) {
        OrderItem orderItem = orderRepository.findOneOrderItem(orderId, itemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find orderItem for itemId " + itemId)
        );
        return orderItemMapper.toDto(orderItem);
    }

    private void createOrder(Long userId, Order order) {
        User userFromBase = userRepository.getReferenceById(userId);
        order.setUser(userFromBase);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find shoppingCart for userId " + userId)
        );
        orderRepository.save(order);
        mappingCartItemToOrderItem(order, shoppingCart.getCartItems());
        BigDecimal total = order.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        orderRepository.save(order);
    }

    private void mappingCartItemToOrderItem(Order order, Set<CartItem> cartItems) {
        Set<OrderItem> orderItemList = new HashSet<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItemList.add(orderItem);
            orderItemRepository.save(orderItem);
        }
        order.getOrderItems().addAll(orderItemList);
    }
}
