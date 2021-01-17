package ru.bechol.jwt.request;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.Hidden;
import ru.bechol.jwt.request.validate.CheckUserExistence;

import javax.validation.constraints.*;

import static ru.bechol.jwt.request.validate.ExistUserValidator.CheckMethod.CHECK_EXISTING_USER;

/**
 * Class RestorePasswordRequest.
 * Serialization of restore password request body.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 * @see ru.bechol.jwt.controller.UserController
 * @see ru.bechol.jwt.services.UserService
 */

public class RestorePasswordRequest extends BaseRequest {

	@Override
	@NotNull(message = NOT_NULL)
	@JsonProperty(required = true)
	@CheckUserExistence(message = USER_NOT_REG, checkMethod = CHECK_EXISTING_USER)
	public String getEmail() {
		return super.getEmail();
	}

	@Hidden
	@JsonIgnore
	@Override
	public @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!_*-+$%]).{6,16}$",
			message = INCORRECT_PASSWORD_MSG) String getPassword() {
		return super.getPassword();
	}
}
