    package com.example.Auth.Controller;

    import com.example.Auth.Service.ChatService;
    import com.example.Auth.entity.ChatSession;
    import com.example.Auth.entity.Message;
    import com.example.Auth.entity.User;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    @RestController
    @RequestMapping("/api/chat")
    public class ChatController {
        @Autowired
        private ChatService chatService;

        @PostMapping("/start/{userId1}/{userId2}")
        public ResponseEntity<ChatSession> startChat(@PathVariable Long userId1, @PathVariable Long userId2) {
            return ResponseEntity.ok(chatService.startChat(userId1, userId2));
        }

        @PostMapping("/send")
        public ResponseEntity<Message> sendMessage(@RequestParam Long chatSessionId, @RequestParam Long senderId, @RequestParam String content) {
            return ResponseEntity.ok(chatService.sendMessage(chatSessionId, senderId, content));
        }

        @GetMapping("/history/{chatSessionId}")
        public ResponseEntity<List<Message>> getChatHistory(@PathVariable Long chatSessionId) {
            return ResponseEntity.ok(chatService.getChatHistory(chatSessionId));
        }

        @PutMapping("/status/{userId}")
        public ResponseEntity<String> updateStatus(@PathVariable Long userId, @RequestParam String status) {
            return ResponseEntity.ok(chatService.updateStatus(userId, status));
        }

        @DeleteMapping("/end/{chatSessionId}")
        public ResponseEntity<String> endChat(@PathVariable Long chatSessionId) {
            chatService.endChat(chatSessionId);
            return ResponseEntity.ok("Chat session ended successfully!");
        }

        @PutMapping("/leave/{chatSessionId}/{userId}")
        public ResponseEntity<String> leaveChat(@PathVariable Long chatSessionId, @PathVariable Long userId) {
            chatService.leaveChat(chatSessionId, userId);
            return ResponseEntity.ok("User left the chat session!");
        }

        @PostMapping("/request/{receiverId}")
        public ResponseEntity<String> sendChatRequest(@PathVariable Long receiverId, @RequestParam Long senderId) {
            chatService.sendChatRequest(senderId, receiverId);
            return ResponseEntity.ok("Chat request sent successfully!");
        }

        @GetMapping("/sessions/{userId}")
        public ResponseEntity<List<ChatSession>> getUserChatSessions(@PathVariable Long userId) {
            return ResponseEntity.ok(chatService.getUserChatSessions(userId));
        }

        @GetMapping("/onlineUsers")
        public ResponseEntity<List<User>> getOnlineUsers() {
            return ResponseEntity.ok(chatService.getOnlineUsers());
        }

        @PutMapping("/request/{requestId}/response")
        public ResponseEntity<String> respondToChatRequest(@PathVariable Long requestId, @RequestParam boolean accepted) {
            chatService.respondToChatRequest(requestId, accepted);
            return ResponseEntity.ok(accepted ? "Chat request accepted!" : "Chat request rejected!");
        }


        @GetMapping("/requests/{userId}")
        public ResponseEntity<List<User>> getPendingChatRequests(@PathVariable Long userId) {
            return ResponseEntity.ok(chatService.getPendingChatRequests(userId));
        }


        @PutMapping("/typing/{chatSessionId}/{userId}")
        public ResponseEntity<String> setTypingStatus(@PathVariable Long chatSessionId, @PathVariable Long userId, @RequestParam boolean isTyping) {
            chatService.setTypingStatus(chatSessionId, userId, isTyping);
            return ResponseEntity.ok("Typing status updated!");
        }


        @DeleteMapping("/message/{messageId}")
        public ResponseEntity<String> deleteMessage(@PathVariable Long messageId) {
            chatService.deleteMessage(messageId);
            return ResponseEntity.ok("Message deleted successfully!");
        }

    }
