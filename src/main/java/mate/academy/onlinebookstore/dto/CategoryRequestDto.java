package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public record CategoryRequestDto(
        @NotEmpty
        String name,
        String description
) {
}
