package ru.bechol.jwt.configuration.security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.bechol.jwt.services.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Class WebSecurityConfig.
 * Spring Security configuration.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	UserService userDetailsService;
	JwtConfig jwtConfig;
	JwtUtils jwtUtils;
	PasswordEncoder passwordEncoder;
	MessageService messageService;
	String loginPath;

	@Autowired
	public WebSecurityConfig(@Qualifier("userServiceImpl") UserService userDetailsService, JwtConfig jwtConfig,
							 JwtUtils jwtUtils, PasswordEncoder passwordEncoder, MessageService messageService,
							 @Value("${security.login-path}") String loginPath) {
		this.userDetailsService = userDetailsService;
		this.jwtConfig = jwtConfig;
		this.jwtUtils = jwtUtils;
		this.passwordEncoder = passwordEncoder;
		this.messageService = messageService;
		this.loginPath = loginPath;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.cors().disable()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling().authenticationEntryPoint(
				(req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
				.and()
				.authorizeRequests()
				.antMatchers("/**").permitAll()
				.and()
				.addFilterBefore(new AuthTokenFilter(jwtConfig, jwtUtils, userDetailsService),
						JwtUsernameAndPasswordAuthFilter.class)
				.addFilterAfter(new JwtUsernameAndPasswordAuthFilter(
								authenticationManager(), messageService, jwtConfig, jwtUtils, loginPath),
						AuthTokenFilter.class)
				.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

}
