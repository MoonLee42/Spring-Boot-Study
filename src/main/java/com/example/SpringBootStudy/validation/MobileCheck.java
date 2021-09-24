package com.example.SpringBootStudy.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = MobileValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MobileCheck {

    String message() default "invalid mobile number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
