package ru.bechol.jwt.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.bechol.jwt.models.User;
import ru.bechol.jwt.request.RegisterRequest;

@Service
public interface UserService extends UserDetailsService {

	ResponseEntity<?> addNewUser(RegisterRequest registerRequest, BindingResult bindingResult);

	ResponseEntity<?> confirmAccount(String confirmationCode);

	User findByEmail(String email);

	ResponseEntity<?> resetUserPassword(String email);
}
