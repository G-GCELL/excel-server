package com.gabia.weat.gcellexcelserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.weat.gcellexcelserver.domain.ExcelData;

public interface ExcelDataRepository extends JpaRepository<ExcelData, Long> {
}
