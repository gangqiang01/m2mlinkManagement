package com.m2m.management.repository;

import com.m2m.management.entity.Repo;
import com.m2m.management.entity.RepoApps;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRepoAppsBean extends JpaRepository<RepoApps, Long> {
    List<RepoApps> findByFilenameContaining(String keyword, Pageable pageable);
    List<RepoApps> findByFilenameContaining(String keyword);
    //name 查询
    Repo findByFilename(String name);
}
