package com.gabia.weat.gcellexcelserver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellexcelserver.error.ErrorCode;
import com.gabia.weat.gcellexcelserver.error.exception.CustomException;
import com.gabia.weat.gcellexcelserver.message.MailMessage;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String origin;

	public void sendMail(String email, String title, String message) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
			mimeMessageHelper.setFrom(new InternetAddress(origin, MailMessage.JOB_MAIL_SENDER, "UTF-8"));
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setSubject(title);
			mimeMessageHelper.setText(message, true);
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.MAIL_SEND_FAIL);
		}
	}

}
