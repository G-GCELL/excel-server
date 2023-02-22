package com.gabia.weat.gcellexcelserver.repository.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gabia.weat.gcellexcelserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellexcelserver.dto.JdbcDto.QuerySetDto;

import org.springframework.stereotype.Component;

@Component
public class StringQueryGenerator implements QueryGenerator {

    @Override
    public QuerySetDto generateQuery(FileCreateRequestDto dto) {
        StringBuilder stringBuilder = new StringBuilder("select ");
        for (String columnName : dto.columnNames()) {
            stringBuilder.append(columnName).append(", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        stringBuilder.append("from excel_data");
        QuerySetDto setDto = getWhereSentence(dto);
        return new QuerySetDto(stringBuilder.append(setDto.sql()).toString(), setDto.params());
    }

    @Override
    public QuerySetDto generateCountQuery(FileCreateRequestDto dto) {
        QuerySetDto setDto = getWhereSentence(dto);
        return new QuerySetDto("select count(*) from excel_data" + setDto.sql(), setDto.params());
    }

    private QuerySetDto getWhereSentence(FileCreateRequestDto dto) {
        StringBuilder stringBuilder;
        List<String> conditions = new ArrayList<>();
        Map<Integer, Object> paramMap = new HashMap<>();
        int paramOrder = 0;

        if (dto.inAccountId() != null && !dto.inAccountId().isEmpty()) {
            if (dto.inAccountId().size() <= 1) {
                dto.inAccountId().add("");
            }
            stringBuilder = new StringBuilder("account_id in (");
            for (String accountId : dto.inAccountId()) {
                paramMap.put(++paramOrder, accountId);
                stringBuilder.append("?,");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            conditions.add(stringBuilder.append(")").toString());
        }

        if (dto.notAccountId() != null && !dto.notAccountId().isEmpty()) {
            stringBuilder = new StringBuilder("account_id not in (");
            for (String accountId : dto.notAccountId()) {
                paramMap.put(++paramOrder, accountId);
                stringBuilder.append("?,");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            conditions.add(stringBuilder.append(")").toString());
        }

        if (dto.inProductCode() != null && !dto.inProductCode().isEmpty()) {
            stringBuilder = new StringBuilder("product_code in (");
            for (String productCode : dto.inProductCode()) {
                paramMap.put(++paramOrder, productCode);
                stringBuilder.append("?,");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            conditions.add(stringBuilder.append(")").toString());
        }

        if (dto.notProductCode() != null && !dto.notProductCode().isEmpty()) {
            stringBuilder = new StringBuilder("product_code not in (");
            for (String productCode : dto.notProductCode()) {
                paramMap.put(++paramOrder, productCode);
                stringBuilder.append("?,");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            conditions.add(stringBuilder.append(")").toString());
        }

        if (dto.startDateMin() != null && dto.startDateMax() != null) {
            conditions.add("(start_date between ? and ?)");
            paramMap.put(++paramOrder, dto.startDateMin());
            paramMap.put(++paramOrder, dto.startDateMax());
        }

        if (dto.endDateMin() != null && dto.endDateMax() != null) {
            conditions.add("(end_date between ? and ?)");
            paramMap.put(++paramOrder, dto.endDateMin());
            paramMap.put(++paramOrder, dto.endDateMax());
        }

        if (dto.costMin() != null && dto.costMax() != null) {
            conditions.add("(cost between ? and ?)");
            paramMap.put(++paramOrder, dto.costMin());
            paramMap.put(++paramOrder, dto.costMax());
        }

        stringBuilder = new StringBuilder();
        if (!conditions.isEmpty()) {
            stringBuilder.append(" where ");
            for (String condition : conditions) {
                stringBuilder.append(condition).append(" and ");
            }
            stringBuilder.delete(stringBuilder.length() - 5, stringBuilder.length());
        }
        return new QuerySetDto(stringBuilder.toString(), paramMap);
    }

    @Override
    public String generateSingleResultQuery() {
        return "select * from excel_data limit 1";
    }

}
