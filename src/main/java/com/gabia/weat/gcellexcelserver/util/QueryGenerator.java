package com.gabia.weat.gcellexcelserver.util;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;

public class QueryGenerator {

	public static String generateQuery(FileCreateRequestDto dto) {
		StringBuilder stringBuilder = new StringBuilder("select * from dataset where 1=1 ");

		if (dto.inAccountId() != null) {
			stringBuilder.append("and account_id in (");
			for (String accountId : dto.inAccountId()) {
				stringBuilder.append(accountId).append(", ");
			}
			stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
			stringBuilder.append(") ");
		}

		if (dto.notAccountId() != null) {
			stringBuilder.append("and account_id not in (");
			for (String accountId : dto.notAccountId()) {
				stringBuilder.append(accountId).append(", ");
			}
			stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
			stringBuilder.append(") ");
		}

		if (dto.inProductCode() != null) {
			stringBuilder.append("and product_code in (");
			for (String productCode : dto.inProductCode()) {
				stringBuilder.append(productCode).append(", ");
			}
			stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
			stringBuilder.append(") ");
		}

		if (dto.notProductCode() != null) {
			stringBuilder.append("and product_code not in (");
			for (String productCode : dto.notProductCode()) {
				stringBuilder.append(productCode).append(", ");
			}
			stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
			stringBuilder.append(") ");
		}

		stringBuilder.append("and (Date(start_date) between '");
		stringBuilder.append(dto.startDateMin());
		stringBuilder.append("' and '");
		stringBuilder.append(dto.startDateMax());
		stringBuilder.append("') ");

		stringBuilder.append("and (Date(end_date) between '");
		stringBuilder.append(dto.endDateMin());
		stringBuilder.append("' and '");
		stringBuilder.append(dto.endDateMax());
		stringBuilder.append("') ");

		stringBuilder.append("and (cost between ");
		stringBuilder.append(dto.costMin());
		stringBuilder.append(" and ");
		stringBuilder.append(dto.costMax());
		stringBuilder.append(")");

		return stringBuilder.toString();
	}

	public static String generateCountQuery(FileCreateRequestDto dto) {
		return generateQuery(dto).replace("*", "count(*)");
	}
}
