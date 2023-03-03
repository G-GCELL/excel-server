package com.gabia.weat.gcellexcelserver.service.log;

import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.dto.log.LogFormatDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConsoleLogPrinter implements LogPrinter {

	@Override
	public void print(LogFormatDto logFormatDto) {
		log.atLevel(logFormatDto.getLevel()).log(logFormatDto.toString());
	}

}