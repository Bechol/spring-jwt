package ru.bechol.jwt.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bechol.jwt.request.ChangeEmailRequest;
import ru.bechol.jwt.services.*;

import javax.validation.Valid;

import static ru.bechol.jwt.response.ErrorMapHelper.createBindingErrorResponse;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/profile")
public class ProfileController {

	ProfileService profileService;

	@Autowired
	public ProfileController(@Qualifier("profileServiceImpl") ProfileService profileService) {
		this.profileService = profileService;
	}

	@PutMapping("/new-email/confirm")
	public ResponseEntity<?> confirmNewEmail(@Valid @RequestBody ChangeEmailRequest changeEmailRequest,
										 BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return createBindingErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
		}
		return profileService.confirmNewEmail(changeEmailRequest);
	}

	@PutMapping("/new-email/{code}")
	public ResponseEntity<?> changeEmail(@PathVariable(name="code") String confirmationCode) {
		if (Strings.isEmpty(confirmationCode)) {
			return ResponseEntity.badRequest().build();
		}
		return profileService.saveNewEmail(confirmationCode);
	}


}
