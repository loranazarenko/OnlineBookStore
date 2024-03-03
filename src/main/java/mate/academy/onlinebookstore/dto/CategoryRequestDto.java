package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.NotEmpty;

public record CategoryRequestDto(
        @NotEmpty
        String name,
        String description
) {
}
