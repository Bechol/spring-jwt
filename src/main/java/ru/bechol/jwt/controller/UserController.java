package ru.bechol.jwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bechol.jwt.request.*;
import ru.bechol.jwt.services.*;

import javax.validation.Valid;

import static ru.bechol.jwt.response.ErrorMapHelper.createBindingErrorResponse;

/**
 * Class UserController.
 * REST controller.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 */
@Slf4j
@Tag(name = "/user", description = "User operations")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/user")
public class UserController {

	UserService userService;
	MessageService messageService;

	@Autowired
	public UserController(@Qualifier("userServiceImpl") UserService userService,
						  MessageService messageService) {
		this.userService = userService;
		this.messageService = messageService;
	}

	/**
	 * Method register.
	 * New user registration.
	 * POST запрос /api/v1/user/register.
	 *
	 * @param registerRequest serialized request body after validation.
	 * @param bindingResult   validation result.
	 * @return BAD_REQUEST if the fields were not validated, if there is a user with the same email;
	 * OK - if registration was successful and a confirmation email was sent to the user.
	 * @see RegisterRequest
	 * @see UserService
	 * @see ru.bechol.jwt.response.Response
	 * @see ru.bechol.jwt.response.ErrorMapHelper
	 */
	@Operation(summary = "registration", description = "new user registration")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "New password sent successfully",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n" +
									"    \"result\": true,\n" +
									"    \"message\": \"We sent confirmation link to your mail. You can login after confirmation.\",\n" +
									"    \"user\": {\n" +
									"        \"username\": \"example@mail.com\",\n" +
									"        \"roles\": [\n" +
									"            \"USER\"\n" +
									"        ]\n" +
									"    }\n" +
									"}")})
					}),
			@ApiResponse(responseCode = "400", description = "Validation errors",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n" +
									"    \"result\": false,\n" +
									"    \"errors\": {\n" +
									"        \"password\": [\n" +
									"            \"password must be 6 to 16 characters long and contain at least: one lowercase letter, one digit i.e. 0-9, one special character (@#!_*-+$%), one capital letter\"\n" +
									"        ],\n" +
									"        \"role\": [\n" +
									"            \"role not valid\"\n" +
									"        ]\n" +
									"    },\n" +
									"    \"message\": \"validation failed\"\n" +
									"}")})
					}),
			@ApiResponse(responseCode = "500", description = "Request body is missing",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": false,\n\t\"message\": " +
									"\"required request body is missing\"\n}")})
					})
	})
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest,
									  BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.warn(messageService.getMessage("validation.errors", registerRequest.getEmail()));
			return createBindingErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
		}
		return userService.addNewUser(registerRequest, bindingResult);
	}

	/**
	 * Method confirm.
	 * User registration confirmation.
	 * Follow the link from the letter.
	 *
	 * @param confirmationCode confirmation code.
	 * @return result of verification code verification. If the check is successful, then the user is activated
	 * and can log in.
	 */
	@Operation(summary = "registration confirmation", description = "new user email confirmation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User successfully registered",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(name = "email confirmed", value = "{\n" +
									"    \"result\": true\n" +
									"}"),
							@ExampleObject(name = "invalid confirmation code", value = "{\n" +
									"    \"result\": false,\n" +
									"    \"message\": \"code not found\"\n" +
									"}"),
							@ExampleObject(name = "empty confirmation code", value = "{\n" +
									"    \"result\": false,\n" +
									"    \"message\": \"confirmation code is null or empty\"\n" +
									"}")
					})
					}),
			@ApiResponse(responseCode = "500", description = "Request body is missing",
					content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
							@ExampleObject(value = "{\n\t\"result\": false,\n\t\"message\": " +
									"\"required request body is missing\"\n}")})
					})
	})
	@GetMapping("/confirm/{confirmationCode}")
	public ResponseEntity<?> confirm(@PathVariable(name = "confirmationCode") String confirmationCode) {
		return userService.confirmAccount(confirmationCode);
	}

}
