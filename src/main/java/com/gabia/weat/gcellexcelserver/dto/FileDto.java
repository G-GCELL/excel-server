package com.gabia.weat.gcellexcelserver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class FileDto {

	public record FileCreateRequestDto(
		@NotNull
		Long memberId,
		@NotBlank
		String fileName,
		@NotEmpty
		List<String> columnNames,
		List<String> inAccountId,
		List<String> notAccountId,
		List<String> inProductCode,
		List<String> notProductCode,
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime startDateMin,
		@NotNull
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime startDateMax,
		@NotNull
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime endDateMin,
		@NotNull
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime endDateMax,
		@NotNull
		BigDecimal costMin,
		@NotNull
		BigDecimal costMax
	) {
	}

}
