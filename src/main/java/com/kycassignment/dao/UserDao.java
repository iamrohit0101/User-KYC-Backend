package com.kycassignment.dao;

import com.kycassignment.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUserName(String userName);
}
