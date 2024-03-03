package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import mate.academy.onlinebookstore.validation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@FieldMatch(password = "password", repeatPassword = "repeatPassword")
public record UserRegistrationRequestDto(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Length(min = 8, max = 35)
        String password,

        @NotBlank
        @Length(min = 8, max = 35)
        String repeatPassword,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        String shippingAddress
) {

}
