package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public record CategoryRequestDto(
        @NotBlank
        String name,
        String description
) {
}
