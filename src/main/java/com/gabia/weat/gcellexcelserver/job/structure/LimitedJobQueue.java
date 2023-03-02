package com.gabia.weat.gcellexcelserver.job.structure;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.gabia.weat.gcellexcelserver.dto.MessageDto.CsvUpdateRequestDto;

public class LimitedJobQueue {

	private final ConcurrentLinkedQueue<CsvUpdateRequestDto> queue = new ConcurrentLinkedQueue<>();
	private final int QUEUE_SIZE_LIMIT = 100;

	public void add(CsvUpdateRequestDto dto) {
		if (!queue.contains(dto)) {
			queue.add(dto);
		}
	}

	public void addManual(CsvUpdateRequestDto dto) {
		if (!queue.contains(dto) && queue.size() < QUEUE_SIZE_LIMIT) {
			queue.add(dto);
		}
	}

	public CsvUpdateRequestDto poll() {
		return queue.poll();
	}

	public boolean contains(CsvUpdateRequestDto dto) {
		return queue.contains(dto);
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

}
