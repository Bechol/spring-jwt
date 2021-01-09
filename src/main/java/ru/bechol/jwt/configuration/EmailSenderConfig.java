package ru.bechol.jwt.configuration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.*;

/**
 * Class EmailSenderConfig.
 * Configuration of email sender.
 *
 * @author Father_BO
 * @email oleg071984@gmail.com
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailSenderConfig {

	String host;
	int port;
	String username;
	String password;
	String protocol;
	String debug;

	public EmailSenderConfig(@Value("${spring.mail.host}") String host,
							 @Value("${spring.mail.port}") int port,
							 @Value("${spring.mail.username}") String username,
							 @Value("${spring.mail.password}") String password,
							 @Value("${spring.mail.protocol}") String protocol,
							 @Value("${mail-sender.debug}") String debug) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.protocol = protocol;
		this.debug = debug;
	}

	@Bean
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		mailSender.setProtocol(protocol);
		mailSender.setDefaultEncoding("UTF-8");
		return mailSender;
	}
}
