package mate.academy.onlinebookstore.dto;

import java.util.Set;

public record BookDto(Long id, String title, String author,
                      String isbn, Double price, String description,
                      String coverImage, Set<Long> categoryIds) {

}
