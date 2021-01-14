package ru.bechol.jwt.request;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.bechol.jwt.request.validate.CheckUserExistence;

import javax.validation.constraints.NotNull;

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

	@CheckUserExistence(message = USER_EXIST, checkMethod = CHECK_NON_EXISTING_USER)
	@JsonProperty(value = "new-email")
	String newEmail;

	@Override
	@NotNull
	@CheckUserExistence(message = USER_NOT_REG, checkMethod = CHECK_EXISTING_USER)
	public String getEmail() {
		return super.getEmail();
	}

}
