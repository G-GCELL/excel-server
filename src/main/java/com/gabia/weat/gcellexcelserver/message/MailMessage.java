package com.gabia.weat.gcellexcelserver.message;

public class MailMessage {

	public static final String JOB_MAIL_SENDER = "Gcell";
	public static final String JOB_COMPLETION_MAIL_TITLE = "[Gcell] 작업이 완료되었습니다";
	public static final String JOB_FAIL_MAIL_TITLE = "[Gcell] 작업이 실패하였습니다.";
	private static final String JOB_COMPLETION_MAIL_PREFIX = "<h4>요청하신 작업이 완료되었습니다</h4><br> 업데이트 작업에 대한 정보는 [ ";
	private static final String JOB_FAIL_MAIL_PREFIX = "<h4>요청하신 작업이 실패하였습니다</h4><br><br> 업데이트 작업에 대한 정보는 [ ";
	private static final String JOB_MAIL_POSTFIX = " ] 입니다.<br><br> 감사합니다";

	public static String success(String methodName, String input) {
		StringBuilder stringBuilder = new StringBuilder(JOB_COMPLETION_MAIL_PREFIX);
		addMethodNameAndInput(stringBuilder, methodName, input);
		stringBuilder.append(JOB_MAIL_POSTFIX);
		return stringBuilder.toString();
	}

	public static String fail(String methodName, String input, String errorMessage) {
		StringBuilder stringBuilder = new StringBuilder(JOB_FAIL_MAIL_PREFIX);
		addMethodNameAndInput(stringBuilder, methodName, input);
		addErrorMessage(stringBuilder, errorMessage);
		stringBuilder.append(JOB_MAIL_POSTFIX);
		return stringBuilder.toString();
	}

	private static void addMethodNameAndInput(StringBuilder stringBuilder, String methodName, String input) {
		stringBuilder.append("method name : ");
		stringBuilder.append(methodName);
		stringBuilder.append(" | input : ");
		stringBuilder.append(input);
	}

	private static void addErrorMessage(StringBuilder stringBuilder, String errorMessage) {
		stringBuilder.append(" | error message : ");
		stringBuilder.append(errorMessage);
	}

}
