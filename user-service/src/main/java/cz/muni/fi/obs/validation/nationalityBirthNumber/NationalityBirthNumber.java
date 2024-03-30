package cz.muni.fi.obs.validation.nationalityBirthNumber;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target( { TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = NationalityBirthNumberValidator.class)
@Documented
public @interface NationalityBirthNumber {
    String message() default "Invalid birth number for given nationality";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String nationalityProperty();
    String birthNumberProperty();
}
