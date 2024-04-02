package cz.muni.fi.obs.validation.nationality;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NationalityValidator implements ConstraintValidator<Nationality, String> {
    @Override
    public void initialize(Nationality constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            cz.muni.fi.obs.enums.Nationality.fromString(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

