package ru.bechol.jwt.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bechol.jwt.exception.UserNotFoundException;
import ru.bechol.jwt.models.User;
import ru.bechol.jwt.request.*;
import ru.bechol.jwt.services.ProfileService;

import javax.validation.Valid;

import static ru.bechol.jwt.response.ErrorMapHelper.createBindingErrorResponse;

/**
 * Class ProfileController.
 * REST controller.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/profile")
public class ProfileController {

	ProfileService profileService;

	@Autowired
	public ProfileController(@Qualifier("profileServiceImpl") ProfileService profileService) {
		this.profileService = profileService;
	}

	/**
	 * Method confirmNewEmail.
	 * PUT request /api/v1/profile/new-email/confirm
	 * Send confirmation link to new email.
	 *
	 * @param changeEmailRequest validated new and "old" email.
	 * @param bindingResult      result of validation.
	 * @return result of operation.
	 */
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR')")
	@PutMapping("/new-email/confirm")
	public ResponseEntity<?> confirmNewEmail(@Valid @RequestBody ChangeEmailRequest changeEmailRequest,
											 BindingResult bindingResult, @AuthenticationPrincipal User user) {
		if (bindingResult.hasErrors()) {
			return createBindingErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
		}
		return profileService.confirmNewEmail(changeEmailRequest, user);
	}

	/**
	 * Method changeEmail.
	 * PUT request /api/v1/profile/new-email/{code}.
	 * Confirmation of new email with saving it as username.
	 *
	 * @param confirmationCode confirmation code.
	 * @return result of operation.
	 * @throws UserNotFoundException if user not found by confirmation code.
	 */
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR')")
	@PutMapping("/new-email/{code}")
	public ResponseEntity<?> changeEmail(@PathVariable(name = "code") String confirmationCode)
			throws UserNotFoundException {
		if (Strings.isEmpty(confirmationCode)) {
			return ResponseEntity.badRequest().build();
		}
		return profileService.saveNewEmail(confirmationCode);
	}

	/**
	 * Method changePassword.
	 * PUT request /api/v1/profile/change/password.
	 * Change user password.
	 *
	 * @param changePasswordRequest serialized request body.
	 * @param bindingResult         validation result of changePasswordRequest fields.
	 * @return result of operation.
	 */
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR')")
	@PutMapping("/change/password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
											BindingResult bindingResult, @AuthenticationPrincipal User user) {
		if (bindingResult.hasErrors()) {
			return createBindingErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
		}
		return profileService.changePassword(changePasswordRequest, user);
	}


}
