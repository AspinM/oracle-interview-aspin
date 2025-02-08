package com.example.Auth.Response;

public class SignUpResponse {
    private boolean success;
    private String message;
    private int status;
    private String statusDescription;

    public SignUpResponse(boolean success, String message, int status, String statusDescription) {
        this.success = success;
        this.message = message;
        this.status = status;
        this.statusDescription = statusDescription;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
