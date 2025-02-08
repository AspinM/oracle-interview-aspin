package com.example.Auth.Service;

import com.example.Auth.Config.JwtUtil;
import com.example.Auth.Repository.UserRepository;
import com.example.Auth.Response.LoginResponse;
import com.example.Auth.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(String username, String password) {
        try {
            if (userRepository.findByUsername(username).isPresent()) {
                throw new RuntimeException("Username already exists");
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return "User SignUp successfully!";
        } catch (Exception ex) {
            throw new RuntimeException("Error during registration: " + ex.getMessage());
        }
    }

    public LoginResponse loginUser(String username, String password) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                if (passwordEncoder.matches(password, user.getPassword())) {
                    // Generate JWT token
                    String token = JwtUtil.generateToken(username);

                    // Update availability status to "Online"
                    user.setAvailabilityStatus("Online");
                    userRepository.save(user); // Save the updated user status

                    return new LoginResponse(username, token, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                }
            }

            throw new RuntimeException("Invalid username or password");
        } catch (Exception ex) {
            throw new RuntimeException("Error during login: " + ex.getMessage());
        }
    }

    public String logoutUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            existingUser.setAvailabilityStatus("Offline"); // Set status to "Offline"
            userRepository.save(existingUser);
            return "User logged out successfully";
        }

        throw new RuntimeException("User not found");
    }
}
