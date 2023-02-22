package com.gabia.weat.gcellexcelserver.service.log;

import com.gabia.weat.gcellexcelserver.dto.log.LogFormatDto;

public interface LogPrinter {

	void print(LogFormatDto logFormatDto);

}