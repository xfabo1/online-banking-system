package cz.muni.fi.obs.validation.nationalityBirthNumber;

import cz.muni.fi.obs.enums.Nationality;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;

public class NationalityBirthNumberValidator implements ConstraintValidator<NationalityBirthNumber, Object> {

    private String nationalityProperty;
    private String birthNumberProperty;

    @Override
    public void initialize(NationalityBirthNumber constraintAnnotation) {
        nationalityProperty = constraintAnnotation.nationalityProperty();
        birthNumberProperty = constraintAnnotation.birthNumberProperty();
    }

    @Override
    public boolean isValid(Object object,
                           ConstraintValidatorContext constraintValidatorContext) {
        String nationalityStr = getPropertyValue(object, nationalityProperty);
        String birthDateStr = getPropertyValue(object, birthNumberProperty);

        if (nationalityStr == null || birthDateStr == null) {
            return false;
        }

        Nationality nationality = Nationality.fromString(nationalityStr);
        try {
            if (nationality == Nationality.SK || nationality == Nationality.CZ) {
                new CzechSlovakBirthNumber(birthDateStr);
            }

            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    private Method getPropertyGetter(Object object, String propertyName) {
        if (propertyName == null || propertyName.isEmpty()) {
            return null;
        }

        String getterName = "get" + propertyName.substring(0, 1).toUpperCase();
        if (propertyName.length() > 1) {
            getterName += propertyName.substring(1);
        }

        try {
            return object.getClass().getMethod(getterName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private String getPropertyValue(Object object, String propertyName) {
        Method valueGetter = getPropertyGetter(object, propertyName);
        if (valueGetter == null) {
            return null;
        }

        try {
            return (String) valueGetter.invoke(object);
        } catch (Throwable e) {
            return null;
        }
    }
}
