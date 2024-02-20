package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mate.academy.onlinebookstore.validation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(password = "password", repeatPassword = "repeatPassword")
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 8, max = 35)
    private String password;

    @NotBlank
    @Length(min = 8, max = 35)
    private String repeatPassword;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String shippingAddress;
}
