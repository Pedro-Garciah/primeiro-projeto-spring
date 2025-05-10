package com.example.demo.domain.user;

public record RegisterDto(String login, String password, UserRole role) {
}
