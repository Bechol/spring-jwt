package ru.bechol.jwt.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.bechol.jwt.request.validate.*;

import javax.validation.constraints.NotNull;

import static ru.bechol.jwt.request.validate.ExistUserValidator.CheckMethod.CHECK_NON_EXISTING_USER;

/**
 * Class RegisterRequest.
 * Used to serialize the request body (RequestBody) into objects of the User, Role classes.
 * Class field values are pre-validated (checked) for compliance with the specified rules.
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 * @see ru.bechol.jwt.models.User
 * @see ru.bechol.jwt.models.Role
 * @see ru.bechol.jwt.controller.UserController
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterRequest extends BaseRequest {

	@CheckRoleExistence
	String role;

	@Override
	@NotNull(message = NOT_NULL)
	@CheckUserExistence(message = USER_EXIST, checkMethod = CHECK_NON_EXISTING_USER)
	public String getEmail() {
		return super.getEmail();
	}

	@Override
	@NotNull(message = NOT_NULL)
	public String getPassword() {
		return super.getPassword();
	}
}
