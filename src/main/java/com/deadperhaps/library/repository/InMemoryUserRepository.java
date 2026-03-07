package com.deadperhaps.library.repository;

import org.mindrot.jbcrypt.BCrypt;
import com.deadperhaps.library.domain.Role;
import com.deadperhaps.library.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();

    public InMemoryUserRepository() {
        users.add(new User("admin", BCrypt.hashpw("admin123", BCrypt.gensalt()), Role.ADMIN));
        users.add(new User("user", BCrypt.hashpw("user123", BCrypt.gensalt()), Role.USER));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public void save(User user) {
        users.add(user);
    }
}