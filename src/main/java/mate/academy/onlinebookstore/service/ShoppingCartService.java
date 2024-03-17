package mate.academy.onlinebookstore.service;

import mate.academy.onlinebookstore.dto.CartItemRequestDto;
import mate.academy.onlinebookstore.dto.CartItemUpdateRequestDto;
import mate.academy.onlinebookstore.dto.ShoppingCartResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartResponseDto addBookToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                                  Authentication authentication);

    ShoppingCartResponseDto getShoppingCart(Pageable pageable,
                                            Authentication authentication);

    ShoppingCartResponseDto update(Long id, CartItemUpdateRequestDto cartItemUpdateRequestDto,
                                   Authentication authentication);

    ShoppingCartResponseDto deleteById(Long id,
                                       Authentication authentication);
}
