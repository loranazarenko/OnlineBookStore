package mate.academy.onlinebookstore.dto;

import java.math.BigDecimal;
import java.util.Set;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public record BookDto(Long id, String title, String author,
                      String isbn, BigDecimal price, String description,
                      String coverImage, Set<Long> categoryIds) {

}
