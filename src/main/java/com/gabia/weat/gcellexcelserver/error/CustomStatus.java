package com.gabia.weat.gcellexcelserver.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomStatus {
	ESSENTIAL_VALUE_ERROR(94000),
	UNKNOWN_ERROR(95000),
	INVALID_COLUMN_NAME(24000),
	INVALID_CSV_HEADER(24001),
	INVALID_CSV_FORMAT(24002),
	RESULT_COUNT_EXCEED(24003),
	NO_RESULT(24041),
	EXCEL_WRITE_FAIL(25001),
	MINIO_UPLOAD_FAIL(25002),
	MINIO_DOWNLOAD_FAIL(25003);

	private final int code;

}
