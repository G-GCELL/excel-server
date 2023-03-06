package com.gabia.weat.gcellexcelserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateJob {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long jobId;
	@Column(nullable = false)
	private String filePath;
	private Integer targetYear;
	private Integer targetMonth;

	private UpdateJob(String filePath, Integer targetYear, Integer targetMonth) {
		this.filePath = filePath;
		this.targetYear = targetYear;
		this.targetMonth = targetMonth;
	}

	public static UpdateJob of(String filePath, Integer year, Integer month) {
		return new UpdateJob(filePath, year, month);
	}

}
