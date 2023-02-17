package com.gabia.weat.gcellexcelserver.service.producer;

public interface Producer<T> {

	void sendMessage(T message);

}
