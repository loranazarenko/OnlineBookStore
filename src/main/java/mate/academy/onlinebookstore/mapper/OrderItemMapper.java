package mate.academy.onlinebookstore.mapper;

import mate.academy.onlinebookstore.config.MapperConfig;
import mate.academy.onlinebookstore.dto.OrderItemResponseDto;
import mate.academy.onlinebookstore.entity.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemResponseDto toDto(OrderItem orderItem);

    OrderItem toModel(OrderItemResponseDto requestDto);

    @AfterMapping
    default OrderItemResponseDto setBookIds(@MappingTarget
                                            OrderItemResponseDto orderItemResponseDto,
                                            OrderItem orderItem) {
        if (orderItem.getBook() == null) {
            return null;
        }
        return new OrderItemResponseDto(
                orderItemResponseDto.id(),
                orderItem.getBook().getId(),
                orderItemResponseDto.quantity()
        );
    }
}
