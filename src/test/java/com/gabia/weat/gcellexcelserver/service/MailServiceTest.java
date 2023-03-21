package com.gabia.weat.gcellexcelserver.service;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

	@Mock
	private MailService mailService;

	@Test
	@DisplayName("메일 서비스를 통해 특정 사용자에게 메일을 전송할 수 있다.")
	public void sendMailTest() {
		// given & when
		mailService.sendMail("test@gabia.com", "testMail", "This is test");

		// then
		then(mailService).should().sendMail(any(), any(), any());
	}

}
