package com.gabia.weat.gcellexcelserver.service.consumer;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import com.rabbitmq.client.Channel;

public interface Consumer<T> {

	void receiveMessage(T fileCreateRequestDto, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws
		IOException,
		SQLException;

}
