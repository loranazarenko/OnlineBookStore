package mate.academy.onlinebookstore.service.impl;

import lombok.AllArgsConstructor;
import mate.academy.onlinebookstore.dto.CartItemRequestDto;
import mate.academy.onlinebookstore.dto.CartItemUpdateRequestDto;
import mate.academy.onlinebookstore.dto.ShoppingCartResponseDto;
import mate.academy.onlinebookstore.entity.ShoppingCart;
import mate.academy.onlinebookstore.entity.User;
import mate.academy.onlinebookstore.mapper.ShoppingCartMapper;
import mate.academy.onlinebookstore.repository.ShoppingCartRepository;
import mate.academy.onlinebookstore.repository.user.UserRepository;
import mate.academy.onlinebookstore.service.CartItemService;
import mate.academy.onlinebookstore.service.ShoppingCartService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemService cartItemService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ShoppingCartResponseDto addBookToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                                         Long userId) {
        ShoppingCart shoppingCart = getShoppingCart(userId);
        cartItemService.findByShoppingCartAndBookId(cartItemRequestDto, shoppingCart, userId);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto findShoppingCart(Pageable pageable, Long userId) {
        return shoppingCartMapper.toDto(getShoppingCart(userId));
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateShoppingCart(Long id, Long userId,
                                                      CartItemUpdateRequestDto
                                                              cartItemUpdateRequestDto) {
        ShoppingCart shoppingCart = getShoppingCart(userId);
        cartItemService.updateCartItem(shoppingCart, id, cartItemUpdateRequestDto.quantity());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto deleteById(Long id, Long userId) {
        ShoppingCart shoppingCart = getShoppingCart(userId);
        cartItemService.removeCartItem(shoppingCart, id);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private ShoppingCart createShoppingCart(Long userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        User userFromBase = userRepository.getReferenceById(userId);
        shoppingCart.setUser(userFromBase);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    private ShoppingCart getShoppingCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseGet(() ->
                createShoppingCart(userId));
    }
}
