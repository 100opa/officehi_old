package com.groupware.officehi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groupware.officehi.dto.WorkDTO;
import com.groupware.officehi.repository.WorkRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class WorkService {

	private final WorkRepository workRepository;

	public void arrivalTimeCheck(WorkDTO work) {
		workRepository.insert(work);
	}

	public void leaveTimeCheck(WorkDTO work) {
		workRepository.update(work);
	}

	public Integer checkDateDuplicte(Long userNo) {
		return workRepository.checkDateDuplicate(userNo);
	}

	public List<WorkDTO> findWorkTimesByUserNo(Long userNo) {
		return workRepository.findWorkTimeByUserNo(userNo);
	}

}
