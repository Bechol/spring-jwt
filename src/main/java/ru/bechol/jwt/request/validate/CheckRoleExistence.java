package ru.bechol.jwt.request.validate;

import ru.bechol.jwt.request.RegisterRequest;

import javax.validation.*;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation CheckRoleExistence.
 * Role field validation.
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 * @see RegisterRequest
 * @see RoleValidator
 */
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = RoleValidator.class)
@Documented
public @interface CheckRoleExistence {

	String message() default "role not valid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
