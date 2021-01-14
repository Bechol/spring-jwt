package ru.bechol.jwt.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bechol.jwt.response.Response;

/**
 * Class GlobalExceptionHandler.
 * Handling of global exceptions.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Response> handleUserNotFoundException(UserNotFoundException exception) {
		return ResponseEntity.badRequest()
				.body(Response.builder().result(false).message(exception.getMessage()).build());
	}
}
