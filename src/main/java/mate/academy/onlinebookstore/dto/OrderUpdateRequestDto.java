package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.NotNull;
import mate.academy.onlinebookstore.entity.Order;

public record OrderUpdateRequestDto(
        @NotNull
        Order.Status status
) {
}
