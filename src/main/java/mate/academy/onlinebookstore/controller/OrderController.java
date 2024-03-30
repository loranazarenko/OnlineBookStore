package mate.academy.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.OrderItemResponseDto;
import mate.academy.onlinebookstore.dto.OrderRequestDto;
import mate.academy.onlinebookstore.dto.OrderResponseDto;
import mate.academy.onlinebookstore.dto.OrderUpdateRequestDto;
import mate.academy.onlinebookstore.entity.User;
import mate.academy.onlinebookstore.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Retrieve user's order history",
            description = "Retrieve user's order history")
    public List<OrderResponseDto> getAllOrdersByUser(Pageable pageable,
                                               Authentication authentication) {
        User user = getUser(authentication);
        return orderService.getAllOrdersByUser(pageable, user.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place an order",
            description = "Place an order")
    public OrderResponseDto saveOrder(@RequestBody @Valid
                                           OrderRequestDto orderRequestDto,
                                                         Authentication authentication) {
        User user = getUser(authentication);
        return orderService.saveOrder(orderRequestDto, user.getId());
    }

    @PatchMapping("/{orderId}")
    @Operation(summary = "Update order status",
            description = "Update order status")
    public void updateOrderStatus(@PathVariable
                                          Long orderId,
                                          @RequestBody @Valid
                                          OrderUpdateRequestDto orderPatchRequestDto) {
        orderService.updateOrderStatus(orderId,
                orderPatchRequestDto);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Retrieve all OrderItems for a specific order",
            description = "Retrieve all OrderItems for a specific order")
    public List<OrderItemResponseDto> getAllItemsByOrder(@PathVariable
                                                             Long orderId) {
        return orderService.getAllItemsByOrder(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Retrieve a specific OrderItem within an order",
            description = "Retrieve a specific OrderItem within an order")
    public OrderItemResponseDto getItemByOrder(@PathVariable
                                                         Long orderId, @PathVariable Long itemId) {
        return orderService.getItemByOrder(orderId, itemId);
    }

    private static User getUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
