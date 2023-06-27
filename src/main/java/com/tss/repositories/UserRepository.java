package com.tss.repositories;

import com.tss.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    @Override
    java.util.List<User> findAll();
}
