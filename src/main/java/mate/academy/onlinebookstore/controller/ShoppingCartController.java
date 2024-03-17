package mate.academy.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.CartItemRequestDto;
import mate.academy.onlinebookstore.dto.CartItemUpdateRequestDto;
import mate.academy.onlinebookstore.dto.ShoppingCartResponseDto;
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
        return shoppingCartService.getShoppingCart(pageable, authentication);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add book to the shopping cart",
            description = "Add book to the shopping cart")
    public ShoppingCartResponseDto addBookToShoppingCart(@RequestBody @Valid
                                                         CartItemRequestDto cartItemRequestDto,
                                                         Authentication authentication) {
        return shoppingCartService.addBookToShoppingCart(cartItemRequestDto, authentication);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update quantity of a book in the shopping cart",
            description = "Update quantity of a book in the shopping cart")
    public ShoppingCartResponseDto update(@PathVariable
                                          Long cartItemId,
                                          @RequestBody @Valid
                                          CartItemUpdateRequestDto cartItemUpdateRequestDto,
                                          Authentication authentication) {
        return shoppingCartService.update(cartItemId, cartItemUpdateRequestDto, authentication);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Remove a book from the shopping cart",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Remove a book from the shopping cart"),
                    @ApiResponse(responseCode = "401", description = "Not found ID"),
            })
    public ShoppingCartResponseDto delete(@PathVariable Long cartItemId,
                                          Authentication authentication) {
        return shoppingCartService.deleteById(cartItemId, authentication);
    }
}
