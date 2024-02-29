package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String author;
    @ISBN
    private String isbn;
    @NotNull
    @Min(value = 0)
    private Double price;
    private String description;
    private String coverImage;
    private Set<Long> categoryIds;
}
