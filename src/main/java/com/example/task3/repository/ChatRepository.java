package com.example.task3.repository;

import com.example.task3.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByThreadIdOrderByCreatedAtAsc(Long threadId);

    @Query("SELECT COUNT(c) FROM Chat c WHERE c.createdAt >= :fromTime")
    long countChatsSince(@Param("fromTime") Instant fromTime);
}
