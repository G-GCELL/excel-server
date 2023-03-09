package com.gabia.weat.gcellexcelserver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.gabia.weat.gcellexcelserver.domain.type.MessageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class MessageDto {

	public record FileCreateRequestMsgDto(
		@NotNull
		Long memberId,
		@NotNull
		Long excelInfoId,
		@NotBlank
		String fileName,
		@NotEmpty
		List<String> columnNames,
		List<String> inAccountId,
		List<String> notAccountId,
		List<String> inProductCode,
		List<String> notProductCode,
		@NotNull
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

	@Builder
	public record FileCreateProgressMsgDto(
		Long memberId,
		Long excelInfoId,
		MessageType messageType,
		String memberFileName,
		Integer progressRate
	) {
	}

	@Builder
	public record FileCreateErrorMsgDto(
		Long memberId,
		Long excelInfoId,
		int errorCode,
		String errorMessage
	) {
	}

	public record CsvUpdateRequestDto(
		@NotBlank
		String fileLocate,
		YearMonth deleteTarget
	) {
	}

}
