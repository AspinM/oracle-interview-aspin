package com.example.Auth.Repository;

import com.example.Auth.entity.ChatSession;
import com.example.Auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    Optional<ChatSession> findByParticipantsContains(User user);
    List<ChatSession> findByParticipants_Id(Long userId);

}

