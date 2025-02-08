package com.example.Auth.Response;

public class LoginResponse {
    private String username;
    private String authToken;
    private int status;
    private String statusDescription;

    public LoginResponse(String username, String authToken, int status, String statusDescription) {
        this.username = username;
        this.authToken = authToken;
        this.status = status;
        this.statusDescription = statusDescription;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
