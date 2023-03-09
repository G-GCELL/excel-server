package com.gabia.weat.gcellexcelserver.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	ESSENTIAL_VALUE_ERROR(CustomStatus.ESSENTIAL_VALUE_ERROR, "올바르지 않은 파라미터 형식입니다."),
	UNKNOWN_ERROR(CustomStatus.UNKNOWN_ERROR, "알 수 없는 오류가 발생하였습니다."),
	NO_RESULT(CustomStatus.NO_RESULT, "조건에 해당하는 결과가 없습니다."),
	EXCEL_WRITE_FAIL(CustomStatus.EXCEL_WRITE_FAIL, "엑셀 파일 작성에 실패하였습니다."),
	INVALID_COLUMN_NAME(CustomStatus.INVALID_COLUMN_NAME, "유효하지 않은 컬럼 이름입니다."),
	MINIO_UPLOAD_FAIL(CustomStatus.MINIO_UPLOAD_FAIL , "파일 업로드에 실패하였습니다."),
	MINIO_DOWNLOAD_FAIL(CustomStatus.MINIO_DOWNLOAD_FAIL , "파일 다운로드에 실패하였습니다."),
	INVALID_CSV_HEADER(CustomStatus.INVALID_CSV_HEADER,"CSV 파일 헤더가 올바르지 않습니다."),
	INVALID_CSV_FORMAT(CustomStatus.INVALID_CSV_FORMAT, "CSV 파일 형식이 올바르지 않습니다.");

	private final CustomStatus customStatus;
	private final String message;

}
