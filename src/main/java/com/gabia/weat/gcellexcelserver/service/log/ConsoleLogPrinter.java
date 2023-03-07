package com.gabia.weat.gcellexcelserver.service.log;

import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.dto.log.LogFormatDto;
import com.gabia.weat.gcellexcelserver.dto.log.LogFormatFactory;
import com.gabia.weat.gcellexcelserver.parser.CustomExpressionParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConsoleLogPrinter extends LogPrinter {

	public ConsoleLogPrinter(LogFormatFactory logFormatFactory,
		CustomExpressionParser expressionBeanParser) {
		super(logFormatFactory, expressionBeanParser);
	}

	@Override
	protected void print(LogFormatDto logFormatDto) {
		log.atLevel(logFormatDto.getLevel()).log(logFormatDto.toString());
	}

}