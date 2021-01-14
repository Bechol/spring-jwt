package ru.bechol.jwt.exception;

/**
 * Class UserNotFoundException.
 * Exception when user not found.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 */
public class UserNotFoundException extends Exception {

	public UserNotFoundException(String message) {
		super(message);
	}
}
