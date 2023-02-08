package com.gabia.weat.gcellexcelserver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FileDto {

	public record FileCreateRequestDto(
		Long memberId,
		String fileName,
		String[] inAccountId,
		String[] notAccountId,
		String[] inProductCode,
		String[] notProductCode,
		LocalDateTime startDateMin,
		LocalDateTime startDateMax,
		LocalDateTime endDateMin,
		LocalDateTime endDateMax,
		BigDecimal costMin,
		BigDecimal costMax
	) {
	}

}
