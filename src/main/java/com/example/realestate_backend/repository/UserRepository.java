package com.example.realestate_backend.repository;

import com.example.realestate_backend.entity.User; // <-- MUST BE YOUR CUSTOM ENTITY
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}