package com.gabia.weat.gcellexcelserver.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomStatus {
	ESSENTIAL_VALUE_ERROR(94000),
	UNKNOWN_ERROR(95000),
	NO_RESULT(24041),
	EXCEL_WRITE_FAIL(25001),
	INVALID_COLUMN_NAME(24000),
	MINIO_UPLOAD_FAIL(25002);

	private int status;

}