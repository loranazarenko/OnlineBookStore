package mate.academy.onlinebookstore.dto;

import lombok.experimental.Accessors;

@Accessors(chain = true)
public record CategoryResponseDto(
        Long id,
        String name,
        String description
) {
}
