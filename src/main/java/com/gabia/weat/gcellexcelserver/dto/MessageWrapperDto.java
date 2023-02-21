package com.gabia.weat.gcellexcelserver.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageWrapperDto<T> {

	@Valid
	private T message;
	private String traceId;

	public static <T> MessageWrapperDto<T> wrapMessageDto(T messageDto, String traceId) {
		return new MessageWrapperDto<>(messageDto, traceId);
	}

}
