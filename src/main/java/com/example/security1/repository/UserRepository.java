package com.example.security1.repository;

import com.example.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// @Repository 필요없음 jpaRepository 안에 있어서
public interface UserRepository extends JpaRepository<User,Integer> {

    public User findByUsername(String userName);
}
