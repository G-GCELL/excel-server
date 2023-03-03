package com.gabia.weat.gcellexcelserver.service.producer;

import com.gabia.weat.gcellexcelserver.dto.MessageWrapperDto;

public interface Producer<T> {

	void sendMessage(MessageWrapperDto<T> message);

}
