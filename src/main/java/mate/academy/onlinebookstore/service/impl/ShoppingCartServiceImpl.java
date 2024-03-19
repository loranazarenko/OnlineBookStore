package mate.academy.onlinebookstore.service.impl;

import lombok.AllArgsConstructor;
import mate.academy.onlinebookstore.dto.CartItemRequestDto;
import mate.academy.onlinebookstore.dto.CartItemUpdateRequestDto;
import mate.academy.onlinebookstore.dto.ShoppingCartResponseDto;
import mate.academy.onlinebookstore.entity.CartItem;
import mate.academy.onlinebookstore.entity.ShoppingCart;
import mate.academy.onlinebookstore.entity.User;
import mate.academy.onlinebookstore.exception.EntityNotFoundException;
import mate.academy.onlinebookstore.mapper.CartItemMapper;
import mate.academy.onlinebookstore.mapper.ShoppingCartMapper;
import mate.academy.onlinebookstore.repository.CartItemRepository;
import mate.academy.onlinebookstore.repository.ShoppingCartRepository;
import mate.academy.onlinebookstore.repository.book.BookRepository;
import mate.academy.onlinebookstore.repository.user.UserRepository;
import mate.academy.onlinebookstore.service.ShoppingCartService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ShoppingCartResponseDto addBookToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                                         Long userId) {
        ShoppingCart shoppingCart = getShoppingCart(userId);
        cartItemRepository.findByShoppingCartAndBookId(shoppingCart, cartItemRequestDto.bookId())
                .ifPresentOrElse(
                        cartItem ->
                                cartItem.setQuantity(cartItem.getQuantity()
                                                     + cartItemRequestDto.quantity()),
                        () -> createCartItem(cartItemRequestDto, shoppingCart)
                );
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto findShoppingCart(
            Pageable pageable,
            Long userId) {
        return shoppingCartMapper.toDto(getShoppingCart(userId));
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateShoppingCart(Long id,
                                                      CartItemUpdateRequestDto
                                                              cartItemUpdateRequestDto,
                                                      Long userId) {
        ShoppingCart shoppingCart = getShoppingCart(userId);
        CartItem cartItem = cartItemRepository.findByShoppingCartAndId(shoppingCart, id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find cart item with id " + id));
        cartItem.setQuantity(cartItemUpdateRequestDto.quantity());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto deleteById(Long id,
                                              Long userId) {
        ShoppingCart shoppingCart = getShoppingCart(userId);
        CartItem cartItem = cartItemRepository.findByShoppingCartAndId(shoppingCart, id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find cart item with id " + id));
        shoppingCart.getCartItems().remove(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private void createCartItem(CartItemRequestDto cartItemRequestDto, ShoppingCart shoppingCart) {
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        cartItem.setBook(bookRepository.getReferenceById(cartItemRequestDto.bookId()));
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
    }

    private ShoppingCart createShoppingCart(Long userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        User userFromBase = userRepository.getReferenceById(userId);
        shoppingCart.setUser(userFromBase);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    private ShoppingCart getShoppingCart(Long userId) {
        ShoppingCart shoppingCart =
                shoppingCartRepository.findByUserId(userId)
                        .orElseGet(() -> createShoppingCart(userId));
        return shoppingCart;
    }
}
