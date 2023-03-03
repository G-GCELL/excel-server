package com.gabia.weat.gcellexcelserver.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	UNKNOWN_ERROR(CustomStatus.UNKNOWN_ERROR, "알 수 없는 오류가 발생하였습니다."),
	NO_RESULT(CustomStatus.NO_RESULT, "조건에 해당하는 결과가 없습니다."),
	EXCEL_WRITE_FAIL(CustomStatus.EXCEL_WRITE_FAIL, "엑셀 파일 작성에 실패하였습니다."),
	INVALID_COLUMN_NAME(CustomStatus.INVALID_COLUMN_NAME, "유효하지 않은 컬럼 이름입니다."),
	MINIO_UPLOAD_FAIL(CustomStatus.MINIO_UPLOAD_FAIL , "파일 업로드에 실패하였습니다."),
	INVALID_CSV_HEADER(null,"CSV 파일 헤더가 옳바르지 않습니다."),
	INVALID_CSV_FORMAT(null, "CSV 파일 형식이 옳바르지 않습니다.");

	private final CustomStatus code;
	private final String message;

}