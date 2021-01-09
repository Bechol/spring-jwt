package ru.bechol.jwt.request;

import ru.bechol.jwt.request.validate.CheckUserExistence;

import javax.validation.constraints.NotNull;

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
	@NotNull
	@CheckUserExistence(message = USER_NOT_REG, checkMethod = CHECK_EXISTING_USER)
	public String getEmail() {
		return super.getEmail();
	}
}
