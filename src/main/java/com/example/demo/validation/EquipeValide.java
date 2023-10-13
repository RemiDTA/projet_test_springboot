package com.example.demo.validation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target({ METHOD, TYPE_PARAMETER, ElementType.TYPE })
@Constraint(validatedBy = EquipeValidator.class)
public @interface EquipeValide {
	String message() default "Le chef d'équipe doit être un membre de l'équipe.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
