package ru.bechol.jwt.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Class MessageService.
 * Messages by locale.
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class MessageService {

	ResourceBundleMessageSource messageSource;

	@Value("${spring.mvc.locale}")
	String localeFromProperties;

	@Autowired
	public MessageService(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String getMessage(String id) {
		Locale locale = new Locale(localeFromProperties);
		return messageSource.getMessage(id, null, locale);
	}

	public String getMessage(String id, Object... param) {
		Locale locale = new Locale(localeFromProperties);
		return messageSource.getMessage(id, param, locale);
	}
}
