package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderRequestDto(@NotBlank
                              String shippingAddress) {
}
