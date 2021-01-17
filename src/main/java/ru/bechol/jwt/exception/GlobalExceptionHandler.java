package ru.bechol.jwt.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import ru.bechol.jwt.response.Response;
import ru.bechol.jwt.services.MessageService;

/**
 * Class GlobalExceptionHandler.
 * Handling of global exceptions.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	MessageService messageService;

	@Autowired
	public GlobalExceptionHandler(MessageService messageService) {
		this.messageService = messageService;
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Response> handleUserNotFoundException(UserNotFoundException exception) {
		return ResponseEntity.status(500).body(Response.builder().result(false)
				.message(messageService.getMessage("not-found.by-code")).build());
	}

	/**
	 * Method handleHttpMessageNotReadableException.
	 *
	 * @param exception HttpMessageNotReadableException when required request body is missing.
	 * @return ResponseEntity with message.
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Response> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
		return ResponseEntity.status(500).body(Response.builder().result(false)
				.message(messageService.getMessage("required-request-body")).build());
	}
}
