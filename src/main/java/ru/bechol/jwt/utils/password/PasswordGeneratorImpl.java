package ru.bechol.jwt.utils.password;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Class PasswordGeneratorImpl.
 * Implementation of PasswordGenerator.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 */
@Component
public class PasswordGeneratorImpl implements PasswordGenerator {

	@Override
	public String generate() {
		return Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()).substring(1, 10);
	}
}
