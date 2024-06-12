package cz.muni.fi.obs.validation.someOptionalPresent;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = SomeOptionalPresentValidator.class)
@Documented
public @interface SomeOptionalPresent {
    String message() default "At least one optional field must be present";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
