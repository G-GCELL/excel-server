package com.gabia.weat.gcellexcelserver.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.weat.gcellexcelserver.domain.UpdateJob;
import com.gabia.weat.gcellexcelserver.dto.MessageDto.CsvUpdateRequestDto;
import com.gabia.weat.gcellexcelserver.repository.UpdateJobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdateJobService {

	private final UpdateJobRepository updateJobRepository;

	public List<UpdateJob> getAllJob() {
		return updateJobRepository.findAll(Sort.by("jobId"));
	}

	@Transactional
	public void addJob(CsvUpdateRequestDto dto) {
		if (notExist(dto)) {
			updateJobRepository.save(UpdateJob.of(
				dto.filePath(),
				dto.deleteTarget().getYear(),
				dto.deleteTarget().getMonth().getValue()
			));
		}
	}

	@Transactional
	public void addJobWithLimit(CsvUpdateRequestDto dto) {
		if (notExist(dto) && updateJobRepository.countBy() < 100) {
			updateJobRepository.save(UpdateJob.of(
				dto.filePath(),
				dto.deleteTarget().getYear(),
				dto.deleteTarget().getMonth().getValue()
			));
		}
	}

	@Transactional
	public void finishJob(Long id) {
		updateJobRepository.deleteById(id);
	}

	private boolean notExist(CsvUpdateRequestDto dto) {
		return !updateJobRepository.existsByFilePathAndTargetYearAndTargetMonth(
			dto.filePath(),
			dto.deleteTarget().getYear(),
			dto.deleteTarget().getMonth().getValue()
		);
	}

}
