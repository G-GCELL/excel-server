package com.gabia.weat.gcellexcelserver.service;

import static org.assertj.core.api.Assertions.*;

import java.time.YearMonth;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.weat.gcellexcelserver.config.RedissonConfig;
import com.gabia.weat.gcellexcelserver.domain.UpdateJob;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.CsvUpdateRequestDto;
import com.gabia.weat.gcellexcelserver.job.JobManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Transactional
@SpringBootTest
class UpdateJobServiceTest {

	@MockBean
	private RedissonConfig redissonConfig;
	@MockBean
	private JobManager jobManager;
	@Autowired
	private UpdateJobService updateJobService;
	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@DisplayName("추가된 작업들을 ID순으로 정렬하여 가져온다")
	void getAllJobTest() {
		// given
		entityManager.persist(UpdateJob.of("data/test1.csv", 2022, 2));
		entityManager.persist(UpdateJob.of("data/test2.csv", 2022, 2));
		entityManager.persist(UpdateJob.of("data/test3.csv", 2022, 2));

		// when
		List<UpdateJob> jobs = updateJobService.getAllJob();

		// then
		assertThat(jobs.size()).isEqualTo(3);
		assertThat(jobs.get(0).getFilePath()).isEqualTo("data/test1.csv");
	}

	@Test
	@DisplayName("작업을 추가한면 작업 테이블에 작업이 추가된다")
	void addJobTest() {
		// given
		UpdateJob job = UpdateJob.of("data/test1.csv", 2022, 2);

		// when
		updateJobService.addJob(new CsvUpdateRequestDto(
			job.getFilePath(), YearMonth.of(job.getTargetYear(), job.getTargetMonth())
		));

		// then
		assertThat(updateJobService.getAllJob().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("수동 작업 추가는 제한이 적용된다")
	void addJobWithLimitTest() {
		// given
		for (int i = 0; i < 100; i++) {
			entityManager.persist(UpdateJob.of("data/test.csv", 2022, 2));
		}
		UpdateJob job = UpdateJob.of("data/additional.csv", 2022, 2);

		// when
		updateJobService.addJobWithLimit(new CsvUpdateRequestDto(
			job.getFilePath(), YearMonth.of(job.getTargetYear(), job.getTargetMonth())
		));

		// then
		assertThat(updateJobService.getAllJob().size()).isEqualTo(100);
	}

	@Test
	@DisplayName("작업을 마치면 ID를 통해 작업을 삭제한다")
	void finishJobTest() {
		// given
		entityManager.persist(UpdateJob.of("data/test1.csv", 2022, 2));
		entityManager.persist(UpdateJob.of("data/test2.csv", 2022, 2));
		List<UpdateJob> jobs = updateJobService.getAllJob();

		// when
		updateJobService.finishJob(jobs.get(0).getJobId());

		// then
		assertThat(updateJobService.getAllJob().size()).isEqualTo(1);
	}

}
