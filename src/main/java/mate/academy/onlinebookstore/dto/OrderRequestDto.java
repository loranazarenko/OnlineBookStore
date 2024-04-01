package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(@NotNull
                              String shippingAddress) {
}
