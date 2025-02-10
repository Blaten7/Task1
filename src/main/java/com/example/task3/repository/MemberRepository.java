package com.example.task3.repository;

import com.example.task3.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.createdAt >= :fromTime")
    long countNewMembersSince(@Param("fromTime") Instant fromTime);
}
