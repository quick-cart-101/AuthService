package com.example.quickcart.repositories;

import com.example.quickcart.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findSessionByToken(String token);
}
