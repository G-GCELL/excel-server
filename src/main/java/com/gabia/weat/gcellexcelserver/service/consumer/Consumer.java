package com.gabia.weat.gcellexcelserver.service.consumer;

import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;

public interface Consumer<T> {

	void receiveMessage(MessageWrapperDto<T> message) throws Exception;

}
