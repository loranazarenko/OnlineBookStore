package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.hibernate.validator.constraints.ISBN;

public record CreateBookRequestDto(
        @NotNull
        String title,
        @NotNull
        String author,
        @ISBN
        String isbn,
        @NotNull
        @Min(value = 0)
        Double price,
        String description,
        String coverImage,
        Set<Long> categoryIds
) {

}
