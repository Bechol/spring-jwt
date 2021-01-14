package ru.bechol.jwt.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.bechol.jwt.exception.UserNotFoundException;
import ru.bechol.jwt.models.User;
import ru.bechol.jwt.request.*;

/**
 * Interface ProfileService.
 * Work with user profile.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 */
@Service
public interface ProfileService {

	ResponseEntity<?> confirmNewEmail(ChangeEmailRequest changeEmailRequest, User user);

	ResponseEntity<?> saveNewEmail(String confirmationCode) throws UserNotFoundException;

	ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest, User user);
}
