package mate.academy.onlinebookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import mate.academy.onlinebookstore.dto.UserRegistrationRequestDto;

public class PasswordValidator implements
        ConstraintValidator<FieldMatch, UserRegistrationRequestDto> {
    private String password;
    private String repeatPassword;

    @Override
    public boolean isValid(UserRegistrationRequestDto request,
                           ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(request.getPassword(), request.getRepeatPassword());
    }
}
