package ru.bechol.jwt.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.bechol.jwt.models.User;
import ru.bechol.jwt.models.*;
import ru.bechol.jwt.repositories.UserRepository;
import ru.bechol.jwt.request.RegisterRequest;
import ru.bechol.jwt.response.Response;
import ru.bechol.jwt.response.dto.UserDto;
import ru.bechol.jwt.services.*;

import java.util.*;

/**
 * Class UserServiceImpl.
 * Implementation of UserService interface.
 *
 * @see User
 * @see UserRepository
 * @see RoleService
 * @see EmailService
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class UserServiceImpl implements UserService {

	UserRepository userRepository;
	RoleService roleService;
	EmailService emailService;
	PasswordEncoder passwordEncoder;
	MessageService messageService;
	String apiContextPath;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, @Qualifier("roleServiceImpl") RoleService roleService,
						   @Qualifier("emailServiceImpl") EmailService emailService, PasswordEncoder passwordEncoder,
						   MessageService messageService,
						   @Value("${server.servlet.contextPath}") String apiContextPath) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.emailService = emailService;
		this.passwordEncoder = passwordEncoder;
		this.messageService = messageService;
		this.apiContextPath = apiContextPath;
	}

	/**
	 * Method addNewUser.
	 * New user creation.
	 *
	 * @param registerRequest deserialized request body.
	 * @param bindingResult   validation result.
	 * @return ResponseEntity<?>.
	 */
	@Override
	public ResponseEntity<?> addNewUser(RegisterRequest registerRequest, BindingResult bindingResult) {
		User user = new User();
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		Role userRole = roleService.findByName(registerRequest.getRole());
		user.setRoles(Collections.singleton(userRole));
		String confirmationCode = UUID.randomUUID().toString();
		user.setCode(confirmationCode);
		user.setEnabled(false);
		emailService.send(user.getEmail(), messageService.getMessage("confirm.email-subject"),
				createConfirmationLink(confirmationCode));
		user = userRepository.save(user);
		return ResponseEntity.ok(Response.builder()
				.result(true)
				.userDto(UserDto.builder()
						.username(user.getUsername())
						.roles(user.getRoles())
						.build())
				.message(messageService.getMessage("email.confirmation-link"))
				.build());
	}

	/**
	 * Method confirmAccount.
	 * Completion of user registration. Account confirmation by means of the confirmation code saved
	 * in the database and the code in the link.
	 *
	 * @param confirmationCode confirmation code.
	 * @return if the account is confirmed, then 200OK and result: true
	 * @see Response
	 */
	@Override
	public ResponseEntity<?> confirmAccount(String confirmationCode) {
		if (!Strings.isNotEmpty(confirmationCode.trim())) {
			return ResponseEntity.ok(Response.builder()
					.result(false)
					.message(messageService.getMessage("confirmation-code.null-or-empty"))
					.build()
			);
		}
		User user = userRepository.findByCode(confirmationCode).orElse(null);
		if (Objects.isNull(user)) {
			log.warn(messageService.getMessage("user-not-found.by-code", confirmationCode));
			return ResponseEntity.ok(Response.builder()
					.result(false)
					.message(messageService.getMessage("code.not-found"))
					.build()
			);
		}
		user.setCode(null);
		user.setEnabled(true);
		userRepository.save(user);
		return ResponseEntity.ok(Response.builder()
				.result(true)
				.build()
		);
	}

	/**
	 * Method createConfirmationLink.
	 * Creating a link to confirm the mailbox address specified during registration.
	 *
	 * @param confirmationCode confirmation code.
	 * @return link to send to the email specified during registration.
	 */
	private String createConfirmationLink(String confirmationCode) {
		return new StringBuffer(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/user/confirm/").toUriString())
				.append(confirmationCode).toString();
	}

	/**
	 * Method findByEmail.
	 * Search for a user by email.
	 *
	 * @param email user email.
	 * @return User.
	 */
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

	/**
	 * Method loadUserByUsername.
	 * Search for a user by username(email).
	 *
	 * @param email user name (email).
	 * @return returns user as authorization object.
	 * @throws UsernameNotFoundException if the user is not found (not logged in)
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(
								messageService.getMessage("user-not-found.by-email", email)
						)
				);
	}
}
