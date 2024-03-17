package mate.academy.onlinebookstore.dto;

public record CartItemRequestDto(
        Long bookId,
        int quantity
) {
}
