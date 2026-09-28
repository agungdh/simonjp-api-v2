package id.my.agungdh.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
@Documented
public @interface Unique {
    String message() default "sudah ada";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String entity();
    String field();
}
