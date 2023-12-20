package com.groupware.officehi.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.groupware.officehi.dto.MyPage;

@Mapper
public interface MyPageRepository {
	List<MyPage> findByAll();
}
