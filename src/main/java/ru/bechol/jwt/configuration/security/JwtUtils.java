package ru.bechol.jwt.configuration.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.bechol.jwt.models.User;
import ru.bechol.jwt.services.MessageService;

import java.util.Date;

/**
 * Class JwtUtils.
 * Methods for working with JWT Token
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 * @see JwtUtils
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class JwtUtils {

	JwtConfig jwtConfig;
	MessageService messageService;

	@Autowired
	public JwtUtils(JwtConfig jwtConfig, MessageService messageService) {
		this.jwtConfig = jwtConfig;
		this.messageService = messageService;
	}

	/**
	 * Method generateJwtToken.
	 * Generation of a JWT token based on the name of the authorized user and settings from the config.
	 *
	 * @param authentication authorized user
	 * @return JWT token.
	 */
	public String generateJwtToken(Authentication authentication) {
		User userPrincipal = (User) authentication.getPrincipal();
		log.info(messageService.getMessage("new.token.generation", userPrincipal.getEmail()));
		return Jwts.builder()
				.setSubject(userPrincipal.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtConfig.getExpiration()))
				.signWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()), SignatureAlgorithm.HS256)
				.compact();
	}

	/**
	 * Method getUserNameFromJwtToken.
	 * Decrypting username from jwt token.
	 *
	 * @param token token from request.
	 * @return username.
	 */
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
				.build()
				.parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * Method validateJwtToken.
	 * Jwt token validation according to several criteria.
	 *
	 * @param jwtToken token from request.
	 * @return true if the token is validated.
	 */
	public boolean validateJwtToken(String jwtToken) {
		try {
			Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
					.build().parseClaimsJws(jwtToken);
			return true;
		} catch (SignatureException e) {
			log.error(messageService.getMessage("invalid.signature", e.getMessage()));
		} catch (MalformedJwtException e) {
			log.error(messageService.getMessage("invalid.token", e.getMessage()));
		} catch (ExpiredJwtException e) {
			log.error(messageService.getMessage("token.expired", e.getMessage()));
		} catch (UnsupportedJwtException e) {
			log.error(messageService.getMessage("token.unsupported", e.getMessage()));
		} catch (IllegalArgumentException e) {
			log.error(messageService.getMessage("claims.empty", e.getMessage()));
		}
		return false;
	}
}
