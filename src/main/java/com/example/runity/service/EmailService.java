package com.example.runity.service;

public interface EmailService {
    void sendEmailVerification(String to, String code);
    void sendPasswordReset(String to, String code);
}
