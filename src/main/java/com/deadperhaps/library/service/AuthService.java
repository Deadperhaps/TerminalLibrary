package com.deadperhaps.library.service;

import org.mindrot.jbcrypt.BCrypt;
import com.deadperhaps.library.domain.User;
import com.deadperhaps.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private User loggedInUser;

    public boolean login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                this.loggedInUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        this.loggedInUser = null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }
}