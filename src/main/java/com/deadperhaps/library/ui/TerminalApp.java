package com.deadperhaps.library.ui;

import com.deadperhaps.library.repository.InMemoryBookRepository;
import com.deadperhaps.library.repository.InMemoryUserRepository;
import com.deadperhaps.library.service.AuthService;
import com.deadperhaps.library.service.LibraryService;

public class TerminalApp {

    public static void main(String[] args) {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryBookRepository bookRepository = new InMemoryBookRepository();

        AuthService authService = new AuthService(userRepository);
        LibraryService libraryService = new LibraryService(bookRepository, authService);

        TerminalView view = new TerminalView();
        TerminalController controller = new TerminalController(authService, libraryService, view);

        controller.run();
    }
}