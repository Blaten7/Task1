package com.example.task3.repository;

import com.example.task3.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByThreadIdOrderByCreatedAtAsc(Long threadId);
}
