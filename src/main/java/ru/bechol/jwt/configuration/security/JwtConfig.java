package ru.bechol.jwt.configuration.security;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class JwtConfig.
 * JWT token options from app config.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 */
@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtConfig {

	String headerName;
	String prefix;
	int expiration;
	String secret;

	public JwtConfig(@Value("${security.jwt.header}") String headerName,
					 @Value("${security.jwt.prefix}") String prefix,
					 @Value("${security.jwt.expiration}") int expiration,
					 @Value("${security.jwt.secret}") String secret) {
		this.headerName = headerName;
		this.prefix = prefix;
		this.expiration = expiration;
		this.secret = secret;
	}
}
