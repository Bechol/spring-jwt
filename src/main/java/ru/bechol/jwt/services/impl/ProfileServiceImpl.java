package ru.bechol.jwt.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.bechol.jwt.models.User;
import ru.bechol.jwt.repositories.UserRepository;
import ru.bechol.jwt.request.ChangeEmailRequest;
import ru.bechol.jwt.response.Response;
import ru.bechol.jwt.services.*;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class ProfileServiceImpl implements ProfileService {

	UserRepository userRepository;
	EmailService emailService;

	@Autowired
	public ProfileServiceImpl(UserRepository userRepository, @Qualifier("emailServiceImpl") EmailService emailService) {
		this.userRepository = userRepository;
		this.emailService = emailService;
	}

	@Override
	public ResponseEntity<?> confirmNewEmail(ChangeEmailRequest changeEmailRequest) {
		User user = userRepository.findByEmail(changeEmailRequest.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("user not found by email"));
		String confirmationCode = UUID.randomUUID().toString().replace("-", "");
		user.setCode(confirmationCode);
		user.setNewEmail(changeEmailRequest.getNewEmail());
		userRepository.save(user);
		emailService.send(changeEmailRequest.getNewEmail(), "Email change", createLink(confirmationCode));
		return ResponseEntity.ok(Response.builder().result(true).message("Confirmation link was send to new email").build());
	}

	@Override
	public ResponseEntity<?> saveNewEmail(String confirmationCode) {
		User user = userRepository.findByCode(confirmationCode)
				.orElseThrow(() -> new UsernameNotFoundException("user not found by confirmation code"));
		user.setEmail(user.getNewEmail());
		user.setCode(null);
		user.setNewEmail(null);
		userRepository.save(user);
		return ResponseEntity.ok(Response.builder().result(true).message("Your email has been successfully changed.").build());
	}

	private String createLink(String confirmationCode) {
		return new StringBuffer(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/new-email/").toUriString())
				.append(confirmationCode).toString();
	}
}
