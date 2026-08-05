package com.sohel.healthcare.repository;

import com.sohel.healthcare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long aLong);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByName(String name);

}
