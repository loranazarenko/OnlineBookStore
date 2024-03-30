package mate.academy.onlinebookstore.mapper;

import mate.academy.onlinebookstore.config.MapperConfig;
import mate.academy.onlinebookstore.dto.OrderRequestDto;
import mate.academy.onlinebookstore.dto.OrderResponseDto;
import mate.academy.onlinebookstore.entity.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class,
        uses = OrderItemMapper.class)
public interface OrderMapper {
    OrderResponseDto toDto(Order order);

    Order toModel(OrderRequestDto requestDto);

    @AfterMapping
    default OrderResponseDto setUserIds(@MappingTarget
                                            OrderResponseDto orderResponseDto,
                                            Order order) {
        if (order.getUser() == null) {
            return null;
        }
        return new OrderResponseDto(
                orderResponseDto.id(),
                order.getUser().getId(),
                orderResponseDto.orderItems(),
                orderResponseDto.orderDate(),
                orderResponseDto.total(),
                orderResponseDto.status()
        );
    }
}
