package ru.bechol.jwt.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.bechol.jwt.request.ChangeEmailRequest;

@Service
public interface ProfileService {

	ResponseEntity<?> confirmNewEmail(ChangeEmailRequest changeEmailRequest);

	ResponseEntity<?> saveNewEmail(String confirmationCode);
}
