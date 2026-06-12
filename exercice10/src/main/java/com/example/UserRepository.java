package com.example;

public interface UserRepository {
    boolean existsByUsername(String username);
    void save(User user);
    User findByUsername(String username);
}
