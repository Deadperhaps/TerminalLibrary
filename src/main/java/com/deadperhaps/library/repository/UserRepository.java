package com.deadperhaps.library.repository;

import com.deadperhaps.library.domain.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    void save(User user);
}