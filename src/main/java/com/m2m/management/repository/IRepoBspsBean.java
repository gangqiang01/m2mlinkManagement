package com.m2m.management.repository;

import com.m2m.management.entity.Repo;
import com.m2m.management.entity.RepoBsp;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRepoBspsBean extends JpaRepository<RepoBsp, Long> {
    List<RepoBsp> findByBoardnameContaining(String keyword, Pageable pageable);
    List<RepoBsp> findByBoardnameContaining(String keyword);
    //name 查询
    Repo findByBoardname(String name);
}