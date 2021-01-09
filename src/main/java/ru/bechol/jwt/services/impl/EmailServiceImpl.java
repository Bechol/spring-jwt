package ru.bechol.jwt.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.bechol.jwt.services.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Class EmailServiceImpl.
 * Implementation of EmailService interface..
 *
 * @author Father_BO
 * @version 1.0
 * @email oleg071984@gmail.com
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Async("asyncExecutor")
@Component
public class EmailServiceImpl implements EmailService {

	JavaMailSender mailSender;

	@Autowired
	public EmailServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Method send.
	 * Sending emails.
	 *
	 * @param emailTo recipient email.
	 * @param subject email subject.
	 * @param message email text.
	 */
	@Override
	public void send(String emailTo, String subject, String message) {
		try {
			MimeMessage mailMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
			helper.setTo(emailTo);
			helper.setSubject(subject);
			mailMessage.setContent(message, "text/html; charset=UTF-8");
			mailSender.send(mailMessage);
		} catch (MessagingException messagingException) {
			log.warn(messagingException.getMessage());
			messagingException.printStackTrace();
		}
	}
}
