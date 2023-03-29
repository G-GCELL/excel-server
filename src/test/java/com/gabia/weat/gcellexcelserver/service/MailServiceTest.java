package com.gabia.weat.gcellexcelserver.service;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

	private MailService mailService;
	private JavaMailSender javaMailSender;
	private MimeMessage mimeMessage;

	@BeforeEach
	public void before() {
		mimeMessage = new MimeMessage((Session)null);
		javaMailSender = mock(JavaMailSender.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
		mailService = new MailService(javaMailSender);
	}

	@Test
	@DisplayName("메일 서비스를 통해 특정 사용자에게 메일을 전송할 수 있다.")
	public void sendMailTest() {
		// given & when
		mailService.sendMail("test@gabia.com", "title", "message");

		// then
		then(javaMailSender).should().send(any(MimeMessage.class));
	}

}
