package com.maolabs.maobank.util.customvalidators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

@Target( { METHOD, FIELD })
@Documented
@Constraint(validatedBy = MaiorDezoitoAnosValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaiorDezoitoAnosContraint {
    String message() default "Usuário deve ser maior de 18 anos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
