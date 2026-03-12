package com.deadperhaps.library.service;

import com.deadperhaps.library.domain.Book;
import com.deadperhaps.library.domain.Role;
import com.deadperhaps.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class LibraryService {
    private final BookRepository bookRepository;
    private final AuthService authService;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByAuthorOrTitle(keyword);
    }

    public void addBook(String title, String author) {
        checkAdminPermission();
        bookRepository.save(new Book(null, title, author));
    }

    public void updateBook(Long id, String newTitle, String newAuthor) {
        checkAdminPermission();
        Book book = new Book(id, newTitle, newAuthor);
        bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        checkAdminPermission();
        bookRepository.deleteById(id);
    }

    private void checkAdminPermission() {
        if (!authService.isLoggedIn() || authService.getLoggedInUser().getRole() != Role.ADMIN) {
            throw new SecurityException("Brak uprawnień. Tylko ADMIN może zarządzać książkami!");
        }
    }
}