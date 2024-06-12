package cz.muni.fi.obs.validation.nationalityBirthNumber;

import cz.muni.fi.obs.data.enums.Nationality;
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
        Nationality nationality = (Nationality) getPropertyValue(object, nationalityProperty);
        String birthNumber = (String) getPropertyValue(object, birthNumberProperty);

        if (nationality == null || birthNumber == null) {
            return false;
        }

        try {

            if (nationality == Nationality.SK || nationality == Nationality.CZ) {
                new CzechSlovakBirthNumber(birthNumber);
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

        try {
            return object.getClass().getMethod(propertyName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private Object getPropertyValue(Object object, String propertyName) {
        Method valueGetter = getPropertyGetter(object, propertyName);
        if (valueGetter == null) {
            return null;
        }

        try {
            return valueGetter.invoke(object);
        } catch (Throwable e) {
            return null;
        }
    }
}
