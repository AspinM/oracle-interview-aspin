package com.example.Auth.Controller;

import com.example.Auth.Response.LoginResponse;
import com.example.Auth.Response.SignUpResponse;
import com.example.Auth.Service.UserService;

import com.example.Auth.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> register(@RequestBody User user) {
        String message = userService.registerUser(user.getUsername(), user.getPassword());
        SignUpResponse response = new SignUpResponse(true, message, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        LoginResponse response = userService.loginUser(user.getUsername(), user.getPassword());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody User user) {
        try {
            String response = userService.logoutUser(user.getUsername());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
