package mate.academy.onlinebookstore.service;

import mate.academy.onlinebookstore.dto.CartItemRequestDto;
import mate.academy.onlinebookstore.dto.CartItemUpdateRequestDto;
import mate.academy.onlinebookstore.dto.ShoppingCartResponseDto;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {
    ShoppingCartResponseDto addBookToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                                  Long userId);

    ShoppingCartResponseDto getShoppingCart(Pageable pageable,
                                            Long userId);

    ShoppingCartResponseDto update(Long id, CartItemUpdateRequestDto cartItemUpdateRequestDto,
                                   Long userId);

    ShoppingCartResponseDto deleteById(Long id,
                                       Long userId);
}
