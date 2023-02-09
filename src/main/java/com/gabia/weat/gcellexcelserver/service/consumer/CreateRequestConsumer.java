package com.gabia.weat.gcellexcelserver.service.consumer;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreateRequestConsumer {

	@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
	public void receiveMessage(FileCreateRequestDto fileCreateRequestDto, Channel channel,
		@Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {

		log.info(String.valueOf(fileCreateRequestDto));
		channel.basicAck(tag, false);
	}

}
