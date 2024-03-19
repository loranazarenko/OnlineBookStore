package mate.academy.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.CartItemRequestDto;
import mate.academy.onlinebookstore.dto.CartItemUpdateRequestDto;
import mate.academy.onlinebookstore.dto.ShoppingCartResponseDto;
import mate.academy.onlinebookstore.entity.User;
import mate.academy.onlinebookstore.service.ShoppingCartService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Retrieve user's shopping cart",
            description = "Retrieve user's shopping cart")
    public ShoppingCartResponseDto getShoppingCart(Pageable pageable,
                                                   Authentication authentication) {
        User user = getUser(authentication);
        return shoppingCartService.findShoppingCart(pageable, user.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add book to the shopping cart",
            description = "Add book to the shopping cart")
    public ShoppingCartResponseDto addBookToShoppingCart(@RequestBody @Valid
                                                         CartItemRequestDto cartItemRequestDto,
                                                         Authentication authentication) {
        User user = getUser(authentication);
        return shoppingCartService.addBookToShoppingCart(cartItemRequestDto, user.getId());
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update quantity of a book in the shopping cart",
            description = "Update quantity of a book in the shopping cart")
    public ShoppingCartResponseDto updateShoppingCart(@PathVariable
                                          Long cartItemId,
                                          @RequestBody @Valid
                                          CartItemUpdateRequestDto cartItemUpdateRequestDto,
                                          Authentication authentication) {
        User user = getUser(authentication);
        return shoppingCartService.updateShoppingCart(cartItemId,
                cartItemUpdateRequestDto, user.getId());
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Remove a book from the shopping cart",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Remove a book from the shopping cart"),
                    @ApiResponse(responseCode = "401", description = "Not found ID"),
            })
    public ShoppingCartResponseDto deleteShoppingCart(@PathVariable Long cartItemId,
                                          Authentication authentication) {
        User user = getUser(authentication);
        return shoppingCartService.deleteById(cartItemId, user.getId());
    }

    private static User getUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
