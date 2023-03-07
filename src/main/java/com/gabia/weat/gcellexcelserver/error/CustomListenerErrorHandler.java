package com.gabia.weat.gcellexcelserver.error;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.converter.MessageDtoConverter;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.FileCreateRequestMsgDto;
import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellexcelserver.error.exception.CustomException;
import com.gabia.weat.gcellexcelserver.service.producer.FileCreateErrorProducer;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomListenerErrorHandler implements RabbitListenerErrorHandler {

	private final FileCreateErrorProducer fileCreateErrorProducer;

	@Override
	public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message,
		ListenerExecutionFailedException exception) throws CustomException {
		MessageWrapperDto<FileCreateRequestMsgDto> messageWrapperDto;
		messageWrapperDto = (MessageWrapperDto<FileCreateRequestMsgDto>)message.getPayload();
		Throwable t = exception.getCause();
		String errorMessage = exception.getMessage();
		ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;

		if (t instanceof CustomException ce) {
			errorMessage = ce.getErrorCode().getMessage();
			errorCode = ce.getErrorCode();
		} else if (t instanceof ConstraintViolationException) {
			errorMessage = ErrorCode.ESSENTIAL_VALUE_ERROR.getMessage();
			errorCode = ErrorCode.ESSENTIAL_VALUE_ERROR;
		}

		fileCreateErrorProducer.sendMessage(
			MessageWrapperDto.wrapMessageDto(
				MessageDtoConverter.toFileCreateErrorMsgDto(messageWrapperDto.getMessage(), errorMessage,
					errorCode.getCustomStatus().getCode()),
				messageWrapperDto.getTraceId())
		);

		throw new CustomException(t, errorCode);
	}

}
