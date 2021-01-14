package ru.bechol.jwt.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.bechol.jwt.exception.UserNotFoundException;
import ru.bechol.jwt.models.User;
import ru.bechol.jwt.repositories.UserRepository;
import ru.bechol.jwt.request.*;
import ru.bechol.jwt.response.Response;
import ru.bechol.jwt.services.*;

import java.util.UUID;

/**
 * Class ProfileServiceImpl.
 * Implementation of ProfileService.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 * @see ProfileService
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class ProfileServiceImpl implements ProfileService {

	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	EmailService emailService;

	@Autowired
	public ProfileServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
							  @Qualifier("emailServiceImpl") EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}

	/**
	 * Method confirmNewEmail.
	 * Sends confirmation link to new email.
	 *
	 * @param changeEmailRequest serialized request body.
	 * @param user               active user.
	 * @return result of operation.
	 */
	@Override
	public ResponseEntity<?> confirmNewEmail(ChangeEmailRequest changeEmailRequest, User user) {
		String confirmationCode = UUID.randomUUID().toString().replace("-", "");
		user.setCode(confirmationCode);
		user.setNewEmail(changeEmailRequest.getNewEmail());
		userRepository.save(user);
		emailService.send(changeEmailRequest.getNewEmail(), "Email change", createLink(confirmationCode));
		return ResponseEntity.ok(Response.builder().result(true).message("Confirmation link was send to new email").build());
	}

	/**
	 * Method saveNewEmail.
	 * Check user by confirmation code and save new email.
	 *
	 * @param confirmationCode confirmation code.
	 * @return result of operation.
	 * @throws UserNotFoundException if user not found by confirmation code.
	 */
	@Override
	public ResponseEntity<?> saveNewEmail(String confirmationCode) throws UserNotFoundException {
		User user = userRepository.findByCode(confirmationCode)
				.orElseThrow(() -> new UserNotFoundException("user not found by confirmation code"));
		user.setEmail(user.getNewEmail());
		user.setCode(null);
		user.setNewEmail(null);
		userRepository.save(user);
		return ResponseEntity.ok(Response.builder().result(true).message("Your email has been successfully changed.").build());
	}

	/**
	 * Method changePassword.
	 * Changes password of active user.
	 *
	 * @param changePasswordRequest serialized request body.
	 * @param user                  active user.
	 * @return result of operation.
	 */
	@Override
	public ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest, User user) {
		user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
		userRepository.save(user);
		emailService.send(user.getEmail(), "Password change", "Your password has been changed.");
		return ResponseEntity.ok(Response.builder().result(true).message("Your password has been changed.").build());
	}

	/**
	 * Method createLink.
	 * Confirmation email link creation.
	 *
	 * @param confirmationCode confirmation code.
	 * @return confirmation link.
	 */
	private String createLink(String confirmationCode) {
		return new StringBuffer(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/new-email/").toUriString())
				.append(confirmationCode).toString();
	}
}
