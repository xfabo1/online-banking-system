package cz.muni.fi.obs.validation.nationality;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NationalityValidator implements ConstraintValidator<Nationality, cz.muni.fi.obs.data.enums.Nationality> {

    @Override
    public boolean isValid(cz.muni.fi.obs.data.enums.Nationality value, ConstraintValidatorContext context) {
        try {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

