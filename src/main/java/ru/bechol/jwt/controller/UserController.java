package ru.bechol.jwt.controller;

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
	@GetMapping("/confirm/{confirmationCode}")
	public ResponseEntity<?> confirm(@PathVariable(name = "confirmationCode") String confirmationCode) {
		return userService.confirmAccount(confirmationCode);
	}

}
