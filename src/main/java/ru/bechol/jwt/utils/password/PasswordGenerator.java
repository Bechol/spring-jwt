package ru.bechol.jwt.utils.password;

import java.util.*;

/**
 * Interface PasswordGenerator.
 * Generating password.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 */
public interface PasswordGenerator {

	default String generate() {
		return Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()).substring(5, 15);
	}
}
