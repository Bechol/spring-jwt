package ru.bechol.jwt.configuration.security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.*;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.bechol.jwt.models.User;
import ru.bechol.jwt.services.UserService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Class AuthTokenFilter.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 * @see JwtConfig
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthTokenFilter extends OncePerRequestFilter {

	JwtConfig jwtConfig;
	JwtUtils jwtUtils;
	UserService userDetailsService;

	@Autowired
	public AuthTokenFilter(JwtConfig jwtConfig, JwtUtils jwtUtils,
						   @Qualifier("userServiceImpl") UserService userDetailsService) {
		this.jwtConfig = jwtConfig;
		this.jwtUtils = jwtUtils;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest,
									@NonNull HttpServletResponse httpServletResponse,
									@NonNull FilterChain filterChain) throws ServletException, IOException {
		String header = httpServletRequest.getHeader(jwtConfig.getHeaderName());
		if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}
		String jwtToken = this.parseAuthorizationHeader(httpServletRequest);
		if (Strings.isNotEmpty(jwtToken) && jwtUtils.validateJwtToken(jwtToken)) {
			String username = jwtUtils.getUserNameFromJwtToken(jwtToken);
			User userDetails = (User) userDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private String parseAuthorizationHeader(HttpServletRequest request) {
		String tokenPrefix = jwtConfig.getPrefix();
		String headerName = jwtConfig.getHeaderName();
		String headerAuth = request.getHeader(headerName);
		if (Strings.isNotEmpty(headerAuth) && headerAuth.startsWith(tokenPrefix)) {
			return headerAuth.replace(tokenPrefix, "");
		}
		return null;
	}
}
