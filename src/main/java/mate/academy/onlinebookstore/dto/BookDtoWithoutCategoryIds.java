package mate.academy.onlinebookstore.dto;

public record BookDtoWithoutCategoryIds(
        Long id,
        String title,
        String author,
        String isbn,
        Double price,
        String description,
        String coverImage
) {
}
