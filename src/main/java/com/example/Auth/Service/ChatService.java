package com.example.Auth.Service;

import com.example.Auth.Repository.ChatRequestRepository;
import com.example.Auth.Repository.ChatSessionRepository;
import com.example.Auth.Repository.MessageRepository;
import com.example.Auth.Repository.UserRepository;
import com.example.Auth.Request.RequestStatus;
import com.example.Auth.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRequestRepository chatRequestRepository;

    public ChatSession startChat(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1).orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(userId2).orElseThrow(() -> new RuntimeException("User not found"));

        ChatSession chatSession = new ChatSession();
        chatSession.setParticipants(Arrays.asList(user1, user2));
        return chatSessionRepository.save(chatSession);
    }

    public Message sendMessage(Long chatSessionId, Long senderId, String content) {
        ChatSession chatSession = chatSessionRepository.findById(chatSessionId)
                .orElseThrow(() -> new RuntimeException("Chat session not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = new Message();
        message.setChatSession(chatSession);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<Message> getChatHistory(Long chatSessionId) {
        return messageRepository.findByChatSessionId(chatSessionId);
    }

    public String updateStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setAvailabilityStatus(status);
        userRepository.save(user); // Save the updated status in the database

        return "Status updated successfully!";
    }

    public void endChat(Long chatSessionId) {
        Optional<ChatSession> chatSession = chatSessionRepository.findById(chatSessionId);
        if (chatSession.isPresent()) {
            chatSessionRepository.delete(chatSession.get()); // Deletes the chat session
        } else {
            throw new RuntimeException("Chat session not found");
        }
    }

    // ✅ Leave a Chat Session (for group chats)
    public void leaveChat(Long chatSessionId, Long userId) {
        ChatSession chatSession = chatSessionRepository.findById(chatSessionId)
                .orElseThrow(() -> new RuntimeException("Chat session not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Remove user from chat session
        chatSession.getParticipants().remove(user);

        // If only one user remains, delete the session
        if (chatSession.getParticipants().size() == 0) {
            chatSessionRepository.delete(chatSession);
        } else {
            chatSessionRepository.save(chatSession);
        }
    }

    // ✅ Send a Chat Request (Optional Feature)
    public void sendChatRequest(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Logic to send a chat request (you can implement WebSocket or database notifications)
        System.out.println("Chat request sent from " + sender.getUsername() + " to " + receiver.getUsername());
    }

    public List<ChatSession> getUserChatSessions(Long userId) {
        return chatSessionRepository.findByParticipants_Id(userId);
    }

    public List<User> getOnlineUsers() {
        return userRepository.findByAvailabilityStatus("Online");
    }

    public void respondToChatRequest(Long requestId, boolean accepted) {
        ChatRequest chatRequest = chatRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Chat request not found!"));

        if (accepted) {
            startChat(chatRequest.getSender().getId(), chatRequest.getReceiver().getId());
            chatRequest.setStatus(RequestStatus.ACCEPTED);
        } else {
            chatRequest.setStatus(RequestStatus.REJECTED);
        }

        chatRequestRepository.save(chatRequest);
    }



    public List<User> getPendingChatRequests(Long userId) {
        List<ChatRequest> requests = chatRequestRepository.findByReceiverId(userId);
        return requests.stream()
                .filter(request -> request.getStatus() == RequestStatus.PENDING)
                .map(ChatRequest::getSender)
                .collect(Collectors.toList());
    }



    private final Map<Long, Map<Long, Boolean>> typingStatus = new HashMap<>();

    public void setTypingStatus(Long chatSessionId, Long userId, boolean isTyping) {
        typingStatus.putIfAbsent(chatSessionId, new HashMap<>());
        typingStatus.get(chatSessionId).put(userId, isTyping);
    }


    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found!"));

        messageRepository.delete(message);
    }






    // ✅ Get All Chat Sessions for a User


}
