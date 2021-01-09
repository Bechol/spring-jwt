package ru.bechol.jwt.request.validate;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import ru.bechol.jwt.services.UserService;

import javax.validation.*;
import java.util.Objects;

/**
 * Class GenderValidator.
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 * @see ru.bechol.jwt.request.RegisterRequest
 * @see ru.bechol.jwt.request.RestorePasswordRequest
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ExistUserValidator implements ConstraintValidator<CheckUserExistence, String> {

	UserService userService;
	CheckMethod checkMethod;

	@Autowired
	public ExistUserValidator(@Qualifier("userServiceImpl") UserService userService) {
		this.userService = userService;
	}

	@Override
	public void initialize(CheckUserExistence constraintAnnotation) {
		checkMethod = constraintAnnotation.checkMethod();
	}

	/**
	 * Method isValid.
	 * Checking the existence of a user by email.
	 *
	 * @param email user email.
	 * @return true if the user with the same email does not exist.
	 */
	@Override
	public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
		return checkMethod.equals(CheckMethod.CHECK_EXISTING_USER) ?
				Objects.nonNull(userService.findByEmail(email)) : Objects.isNull(userService.findByEmail(email));
	}

	public enum CheckMethod {
		CHECK_EXISTING_USER, CHECK_NON_EXISTING_USER
	}
}
