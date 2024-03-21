package mate.academy.onlinebookstore.mapper;

import mate.academy.onlinebookstore.config.MapperConfig;
import mate.academy.onlinebookstore.dto.CartItemRequestDto;
import mate.academy.onlinebookstore.dto.CartItemResponseDto;
import mate.academy.onlinebookstore.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem book);

    CartItem toModel(CartItemRequestDto requestDto);
}
