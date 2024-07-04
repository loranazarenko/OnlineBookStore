package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public record CreateBookRequestDto(
        @NotNull
        String title,
        @NotNull
        String author,
        @NotNull
        String isbn,
        @NotNull
        @Min(value = 0)
        Double price,
        String description,
        String coverImage,
        Set<Long> categoryIds
) {

}
