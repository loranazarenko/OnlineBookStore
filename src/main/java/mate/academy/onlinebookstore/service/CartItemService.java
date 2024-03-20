package mate.academy.onlinebookstore.service;

import mate.academy.onlinebookstore.dto.CartItemRequestDto;
import mate.academy.onlinebookstore.entity.ShoppingCart;

public interface CartItemService {
    void findByShoppingCartAndBookId(CartItemRequestDto cartItemRequestDto,
                                     ShoppingCart shoppingCart, Long id);

    void updateCartItem(ShoppingCart shoppingCart, Long id, int quantity);

    void removeCartItem(ShoppingCart shoppingCart, Long id);
}
