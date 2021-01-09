package ru.bechol.jwt.request.validate;

import ru.bechol.jwt.request.RegisterRequest;

import javax.validation.*;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.bechol.jwt.request.validate.ExistUserValidator.CheckMethod;

/**
 * Annotation CheckRoleExistence.
 * User name validation.
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 * @see RegisterRequest
 * @see RoleValidator
 */
@Target({TYPE, FIELD, METHOD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ExistUserValidator.class)
@Documented
public @interface CheckUserExistence {

	String message() default "user already exist";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	CheckMethod checkMethod() default CheckMethod.CHECK_EXISTING_USER;
}
