package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotEmpty
    private String name;
    private String description;
}
