package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateValidation, LocalDate> {

    private static final LocalDate FIRST_ALLOWED_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(ReleaseDateValidation constraintAnnotation) {
        // Если вдруг инициализация нужна, но в этом случае не требуется
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return (date == null) || (!date.isBefore(FIRST_ALLOWED_DATE));
    }
}
