package com.m2m.management.repository;

import com.m2m.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IUserBean extends JpaRepository<User, Long> {
    User findUserByname(String username);
    @Query("select u from User u where u.uid=?1")
    User findByUid(Long Uid);
}
