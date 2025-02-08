package com.example.Auth.Repository;


import com.example.Auth.entity.ChatRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatRequestRepository extends JpaRepository<ChatRequest, Long> {
    List<ChatRequest> findByReceiverId(Long receiverId);
}
