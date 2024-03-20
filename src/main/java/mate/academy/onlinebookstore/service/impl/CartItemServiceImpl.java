package mate.academy.onlinebookstore.service.impl;

import lombok.AllArgsConstructor;
import mate.academy.onlinebookstore.dto.CartItemRequestDto;
import mate.academy.onlinebookstore.entity.CartItem;
import mate.academy.onlinebookstore.entity.ShoppingCart;
import mate.academy.onlinebookstore.exception.EntityNotFoundException;
import mate.academy.onlinebookstore.mapper.CartItemMapper;
import mate.academy.onlinebookstore.repository.CartItemRepository;
import mate.academy.onlinebookstore.repository.book.BookRepository;
import mate.academy.onlinebookstore.service.CartItemService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public void findByShoppingCartAndBookId(CartItemRequestDto cartItemRequestDto,
                                            ShoppingCart shoppingCart, Long id) {
        cartItemRepository.findByShoppingCartAndBookId(shoppingCart, cartItemRequestDto.bookId())
                .ifPresentOrElse(
                        cartItem ->
                                cartItem.setQuantity(cartItem.getQuantity()
                                                     + cartItemRequestDto.quantity()),
                        () -> createCartItem(cartItemRequestDto, shoppingCart)
                );
    }

    @Override
    public void updateCartItem(ShoppingCart shoppingCart, Long id, int quantity) {
        CartItem cartItem = cartItemRepository.findByShoppingCartAndBookId(shoppingCart, id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find cart item with id " + id));
        cartItem.setQuantity(quantity);
    }

    @Override
    public void removeCartItem(ShoppingCart shoppingCart, Long id) {
        CartItem cartItem = cartItemRepository.findByShoppingCartAndId(shoppingCart, id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find cart item with id " + id));
        shoppingCart.getCartItems().remove(cartItem);
    }

    private void createCartItem(CartItemRequestDto cartItemRequestDto, ShoppingCart shoppingCart) {
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        cartItem.setBook(bookRepository.getReferenceById(cartItemRequestDto.bookId()));
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
    }
}
