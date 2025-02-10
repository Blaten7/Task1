package com.example.task3.repository;

import com.example.task3.entity.Feedback;
import com.example.task3.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findByMember(Member member, Pageable pageable);
    Page<Feedback> findByMemberAndIsPositive(Member member, boolean isPositive, Pageable pageable);
    Page<Feedback> findByIsPositive(boolean isPositive, Pageable pageable);
}
