package ru.bechol.jwt.request;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;


/**
 * Class ChangePasswordRequest.
 * Serialization of change password request body.
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
public class ChangePasswordRequest extends BaseRequest {

	@JsonProperty(value = "new-password")
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!_*-+$%]).{6,16}$",
			message = INCORRECT_PASSWORD_MSG)
	String newPassword;
}
