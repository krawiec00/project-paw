package com.tss.repositories;

import com.tss.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    
    @Override
    List<User> findAll(); // Updated method name
}
