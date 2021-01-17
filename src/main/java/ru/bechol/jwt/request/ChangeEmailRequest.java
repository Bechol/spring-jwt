package ru.bechol.jwt.request;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.bechol.jwt.request.validate.CheckUserExistence;

import javax.validation.constraints.*;

import static ru.bechol.jwt.request.validate.ExistUserValidator.CheckMethod.CHECK_EXISTING_USER;
import static ru.bechol.jwt.request.validate.ExistUserValidator.CheckMethod.CHECK_NON_EXISTING_USER;


/**
 * Class ChangeEmailRequest.
 * Serialization of change email request body.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 * @see ru.bechol.jwt.controller.ProfileController
 * @see ru.bechol.jwt.services.ProfileService
 */

@Data
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangeEmailRequest extends BaseRequest {

	@NotNull(message = NOT_NULL)
	@Pattern(regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",
			message = INCORRECT_EMAIL_MSG)
	@CheckUserExistence(message = USER_EXIST, checkMethod = CHECK_NON_EXISTING_USER)
	@JsonProperty(required = true, value = "new-email")
	String newEmail;

	@Override
	@NotNull(message = NOT_NULL)
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
