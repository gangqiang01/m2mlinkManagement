package com.m2m.management.repository;

import com.m2m.management.entity.Repo;
import com.m2m.management.entity.RepoApp;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRepoAppsBean extends JpaRepository<RepoApp, Long> {
    List<RepoApp> findByFilenameContaining(String keyword, Pageable pageable);
    List<RepoApp> findByFilenameContaining(String keyword);
    //name 查询
    Repo findByFilename(String name);
}
