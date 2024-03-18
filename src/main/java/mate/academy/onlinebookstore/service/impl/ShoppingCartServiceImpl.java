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
        ShoppingCart shoppingCart =
                shoppingCartRepository.findByUserId(userId);
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            User userFromBase = userRepository.getReferenceById(userId);
            shoppingCart.setUser(userFromBase);
            shoppingCartRepository.save(shoppingCart);
        }

        ShoppingCart copyShoppingCart = shoppingCart;
        cartItemRepository.findByShoppingCartAndBookId(shoppingCart, cartItemRequestDto.bookId())
                .ifPresentOrElse(
                        cir -> cir.setQuantity(cir.getQuantity() + cartItemRequestDto.quantity()),
                        () -> createCartItem(cartItemRequestDto, copyShoppingCart)
                );
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private void createCartItem(CartItemRequestDto cartItemRequestDto, ShoppingCart shoppingCart) {
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        cartItem.setBook(bookRepository.getReferenceById(cartItemRequestDto.bookId()));
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto getShoppingCart(
            Pageable pageable,
            Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto update(Long id,
                                          CartItemUpdateRequestDto cartItemUpdateRequestDto,
                                          Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
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
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        CartItem cartItem = cartItemRepository.findByShoppingCartAndId(shoppingCart, id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find cart item with id " + id));
        shoppingCart.getCartItems().remove(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }
}
