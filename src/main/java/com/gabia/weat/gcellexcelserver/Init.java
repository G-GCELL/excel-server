package com.gabia.weat.gcellexcelserver;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.gabia.weat.gcellexcelserver.file.reader.StreamCsvParser;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Init {

	private final StreamCsvParser streamCsvParser;

	@PostConstruct
	public void init() throws IOException {
		// ⚠️ caution: Large Dataset Insert ↓ ↓ ↓
		// streamCsvParser.insertWithCsv("202201-202212.csv");
	}

}
