package com.gabia.weat.gcellexcelserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.weat.gcellexcelserver.domain.UpdateJob;

public interface UpdateJobRepository extends JpaRepository<UpdateJob, Long> {

	boolean existsByFilePathAndTargetYearAndTargetMonth(String filePath, int year, int month);

	Long countBy();

}
