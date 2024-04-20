package cz.muni.fi.obs.validation.someOptionalPresent;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.Optional;

public class SomeOptionalPresentValidator implements ConstraintValidator<SomeOptionalPresent, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        Field[] fields = value.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Optional.class.isAssignableFrom(field.getType())) {
                try {
                    field.setAccessible(true);
                    Optional<?> optional = (Optional<?>) field.get(value);
                    if (optional.isPresent()) {
                        return true;
                    }
                } catch (IllegalAccessException e) {
                    return false;
                }
            }
        }

        return false;
    }
}
