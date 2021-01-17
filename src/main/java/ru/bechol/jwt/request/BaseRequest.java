package ru.bechol.jwt.request;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;

import javax.validation.constraints.Pattern;

/**
 * Class BaseRequest.
 * Base class for serialization of request bodies.
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 * @see RegisterRequest
 * @see RestorePasswordRequest
 */
@Getter
public abstract class BaseRequest {

	final static String INCORRECT_PASSWORD_MSG = "password must be 6 to 16 characters long and contain at least: " +
			"one lowercase letter, one digit i.e. 0-9, one special character (@#!_*-+$%), one capital letter";
	final static String INCORRECT_EMAIL_MSG = "incorrect email";
	final static String USER_EXIST = "user with this email is already exist";
	final static String USER_NOT_REG = "user with this email is not registered";
	final static String NOT_NULL = "should not be null";

	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!_*-+$%]).{6,16}$",
			message = INCORRECT_PASSWORD_MSG)
	String password;

	@Pattern(regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",
			message = INCORRECT_EMAIL_MSG)
	String email;

}
