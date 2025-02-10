package com.example.task3.repository;

import com.example.task3.entity.ChatThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatThreadRepository extends JpaRepository<ChatThread, Long> {
    Optional<ChatThread> findTopByMemberIdOrderByCreatedAtDesc(Long memberId);
}