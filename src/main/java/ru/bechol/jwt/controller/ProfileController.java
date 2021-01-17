package ru.bechol.jwt.controller;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.bechol.jwt.response.Response;
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
@Tag(name = "/profile", description = "User profile api operations")
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
	 * @param user               active user
	 * @param bindingResult      result of validation.
	 * @return result of operation.
	 */
	@Operation(summary = "change user email", description = "send confirmation link to new user email")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Confirmation link sent successfully",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": true,\n\t\"message\": " +
									"\"confirmation link was send to new email\"\n}")})
					}),
			@ApiResponse(responseCode = "400", description = "Validation errors",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": false,\n\t\"errors\": {\n" +
									"\t\t\"new-email\": [\n\"incorrect email\",\n" +
									"\"user with this email is already exist\"\n]," +
									"\t\t\"email\": [\n\"incorrect email\",\n" +
									"\"user with this email is not registered\"\n] " +
									"\n},\"message\": \"validation failed\"\n}")})
					}),
			@ApiResponse(responseCode = "500", description = "Request body is missing",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": false,\n\t\"message\": " +
									"\"required request body is missing\"\n}")})
					})
	})
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR')")
	@PutMapping("/email/change")
	public ResponseEntity<?> confirmNewEmail(@Valid @RequestBody ChangeEmailRequest changeEmailRequest,
											 BindingResult bindingResult,
											 @Parameter(hidden = true) @AuthenticationPrincipal User user) {
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
	@Operation(summary = "confirm new user email", description = "confirmation of new email with saving it as username")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The new email was successfully confirmed and saved",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": true,\n\t\"message\": " +
									"\"your email has been successfully changed\"\n}")})
					}),
			@ApiResponse(responseCode = "403", description = "Confirmation code is null or empty",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": false,\n\t\"message\": " +
									"\"confirmation code is null or empty\"\n}")})
					}),
			@ApiResponse(responseCode = "500", description = "User not found by confirmation code",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": false,\n\t\"message\": " +
									"\"user not found by confirmation code\"\n}")})
					})
	})
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR')")
	@PutMapping("/email/{code}")
	public ResponseEntity<?> changeEmail(@PathVariable(name = "code") String confirmationCode)
			throws UserNotFoundException {
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
	@Operation(summary = "change password", description = "Change user password and send email notification to user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The new password was successfully saved",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": true,\n\t\"message\": " +
									"\"your password has been changed\"\n}")})
					}),
			@ApiResponse(responseCode = "400", description = "Validation errors",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": false,\n\t\"errors\": {\n" +
									"\t\t\"new-password\": [\n\"incorrect password\",\n" +
									"\"password must be 6 to 16 characters long and contain at least: one lowercase " +
									"letter, one digit i.e. 0-9, one special character (@#!_*-+$%), one capital letter\"\n] " +
									"\n},\"message\": \"validation failed\"\n}")})
					}),
			@ApiResponse(responseCode = "500", description = "Request body is missing",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": false,\n\t\"message\": " +
									"\"required request body is missing\"\n}")})
					})
	})
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR')")
	@PutMapping("/change/password")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
											BindingResult bindingResult,
											@Parameter(hidden = true) @AuthenticationPrincipal User user) {
		if (bindingResult.hasErrors()) {
			return createBindingErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
		}
		return profileService.changePassword(changePasswordRequest, user);
	}


}
