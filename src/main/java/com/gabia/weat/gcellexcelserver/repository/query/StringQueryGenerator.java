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
        StringBuilder stringBuilder;
        List<String> conditions = new ArrayList<>();
        Map<Integer, Object> paramMap = new HashMap<>();
        int paramOrder = 0;

        if (dto.inAccountId() != null) {
            stringBuilder = new StringBuilder("account_id in (");
            for (String accountId : dto.inAccountId()) {
                paramMap.put(++paramOrder, accountId);
                stringBuilder.append("?,");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            conditions.add(stringBuilder.append(")").toString());
        }

        if (dto.notAccountId() != null) {
            stringBuilder = new StringBuilder("account_id not in (");
            for (String accountId : dto.notAccountId()) {
                paramMap.put(++paramOrder, accountId);
                stringBuilder.append("?,");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            conditions.add(stringBuilder.append(")").toString());
        }

        if (dto.inProductCode() != null) {
            stringBuilder = new StringBuilder("product_code in (");
            for (String productCode : dto.inProductCode()) {
                paramMap.put(++paramOrder, productCode);
                stringBuilder.append("?,");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            conditions.add(stringBuilder.append(")").toString());
        }

        if (dto.notProductCode() != null) {
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

        String sql = "select * from excel_data";
        if (conditions.size() > 0) {
            stringBuilder = new StringBuilder(sql);
            stringBuilder.append(" where ");
            for (String condition : conditions) {
                stringBuilder.append(condition).append(" and ");
            }
            sql = stringBuilder.substring(0, stringBuilder.length() - 5);
        }
        return new QuerySetDto(sql, paramMap);
    }

    @Override
    public QuerySetDto generateCountQuery(FileCreateRequestDto dto) {
        QuerySetDto querySet = generateQuery(dto);
        return new QuerySetDto(querySet.sql().replace("*", "count(*)"), querySet.params());
    }

}
