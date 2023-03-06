package com.gabia.weat.gcellexcelserver.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long excelDataId;
	@Column(length = 10)
	private String accountId;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String productCode;
	@Column(columnDefinition = "DECIMAL(30, 20)", nullable = false)
	private BigDecimal cost;

	private ExcelData(String accountId, LocalDateTime startDate, LocalDateTime endDate, String productCode,
		BigDecimal cost) {
		this.accountId = accountId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.productCode = productCode;
		this.cost = cost;
	}

	public static ExcelData createWithoutId(String accountId,
		LocalDateTime startDate,
		LocalDateTime endDate,
		String productCode,
		BigDecimal cost) {
		return new ExcelData(accountId, startDate, endDate, productCode, cost);
	}

}
