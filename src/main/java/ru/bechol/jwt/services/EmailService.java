package ru.bechol.jwt.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Async
@Service
public interface EmailService {

	void send(String emailTo, String subject, String message);
}
