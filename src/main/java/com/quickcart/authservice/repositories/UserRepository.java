package com.example.quickcart.repositories;

import com.example.quickcart.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Boolean existsUserByEmail(String email);

    Boolean existsUserByContactNumber(String contactNumber);

    Optional<User> findUserByEmail(String email);
}
