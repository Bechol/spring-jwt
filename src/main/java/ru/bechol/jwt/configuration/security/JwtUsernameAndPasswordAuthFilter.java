package ru.bechol.jwt.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.bechol.jwt.models.User;
import ru.bechol.jwt.response.Response;
import ru.bechol.jwt.response.dto.UserDto;
import ru.bechol.jwt.services.MessageService;

import javax.servlet.FilterChain;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Class JwtUsernameAndPasswordAuthFilter.
 * Username and password auth filter.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtUsernameAndPasswordAuthFilter extends UsernamePasswordAuthenticationFilter {


	AuthenticationManager authManager;
	JwtConfig jwtConfig;
	JwtUtils jwtUtils;
	MessageService messageService;
	String loginPath;

	@Autowired
	public JwtUsernameAndPasswordAuthFilter(AuthenticationManager authManager, MessageService messageService,
											JwtConfig jwtConfig, JwtUtils jwtUtils,
											@Value("${security.login-path}") String loginPath) {
		this.authManager = authManager;
		this.messageService = messageService;
		this.jwtConfig = jwtConfig;
		this.jwtUtils = jwtUtils;
		this.loginPath = loginPath;
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(this.loginPath, "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			UserCredentials userCredentials = new ObjectMapper()
					.readValue(request.getInputStream(), UserCredentials.class);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					userCredentials.getEmail(), userCredentials.getPassword());
			return authManager.authenticate(authToken);
		} catch (IOException e) {
			log.error(messageService.getMessage("le.auth.failed"));
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											FilterChain chain, Authentication authResult) throws IOException {
		String jwtToken = jwtUtils.generateJwtToken(authResult);
		User authorizedUser = (User) authResult.getPrincipal();
		response.addHeader(jwtConfig.getHeaderName(), jwtConfig.getPrefix() + jwtToken);
		response.getWriter().println(new ObjectMapper()
				.writerWithDefaultPrettyPrinter().writeValueAsString(
						Response.builder()
								.result(true)
								.token(jwtConfig.getPrefix() + jwtToken)
								.userDto(UserDto.builder()
										.username(authorizedUser.getUsername())
										.roles(authorizedUser.getRoles())
										.build())
								.build()
				)
		);
		response.setContentType(MediaType.APPLICATION_JSON.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		SecurityContextHolder.getContext().setAuthentication(authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											  AuthenticationException failed) throws IOException {
		response.getWriter().println(new ObjectMapper()
				.writerWithDefaultPrettyPrinter().writeValueAsString(
						Response
								.builder()
								.result(false)
								.message(failed.getMessage())
								.build()
				)
		);
		response.setContentType(MediaType.APPLICATION_JSON.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
	}

	@Data
	private static class UserCredentials {
		private String email, password;
	}
}
