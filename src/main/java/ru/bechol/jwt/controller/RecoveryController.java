package ru.bechol.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bechol.jwt.request.RestorePasswordRequest;
import ru.bechol.jwt.services.UserService;

import javax.validation.Valid;

import static ru.bechol.jwt.response.ErrorMapHelper.createBindingErrorResponse;

@Tag(name = "/recovery", description = "Restoring user access")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/recovery")
public class RecoveryController {

	UserService userService;

	@Autowired
	public RecoveryController(@Qualifier("userServiceImpl") UserService userService) {
		this.userService = userService;
	}

	/**
	 * Method restore.
	 * PUT request /api/v1/recovery/password.
	 * Recover forgot user password.
	 *
	 * @param restorePasswordRequest serialized recovery request body.
	 * @return result of request.
	 */
	@Operation(summary = "restoring user password", description = "check user email and send new password")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "New password sent successfully",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": true,\n\t\"message\": " +
									"\"new password sent to your email\"\n}")})
					}),
			@ApiResponse(responseCode = "400", description = "Validation errors",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": false,\n\t\"errors\": {\n" +
									"\t\t\"email\": [\n\"incorrect email\",\n" +
									"\"user with this email is not registered\"\n]" +
									"\n},\"message\": \"validation failed\"\n}")})
					}),
			@ApiResponse(responseCode = "500", description = "Request body is missing",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": false,\n\t\"message\": " +
									"\"required request body is missing\"\n}")})
					})
	})
	@PutMapping("/password")
	public ResponseEntity<?> restore(@Valid @RequestBody RestorePasswordRequest restorePasswordRequest,
									 BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return createBindingErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
		}
		return userService.resetUserPassword(restorePasswordRequest.getEmail());
	}
}
