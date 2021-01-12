package ru.bechol.jwt.controller;

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

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/recovery")
public class RecoveryController {

	UserService userService;

	@Autowired
	public RecoveryController(@Qualifier("userServiceImpl") UserService userService) {
		this.userService = userService;
	}

	@PutMapping("/password")
	public ResponseEntity<?> restore(@Valid @RequestBody RestorePasswordRequest restorePasswordRequest,
									 BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return createBindingErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
		}
		return userService.resetUserPassword(restorePasswordRequest.getEmail());
	}
}
