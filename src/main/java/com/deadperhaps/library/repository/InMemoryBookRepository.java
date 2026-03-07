package com.deadperhaps.library.repository;

import com.deadperhaps.library.domain.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryBookRepository implements BookRepository {
    private final List<Book> books = new ArrayList<>();
    private Long idCounter = 1L;

    public InMemoryBookRepository() {
        save(new Book(null, "Wladca Pierscieni", "J.R.R. Tolkien"));
        save(new Book(null, "Wiedzmin", "Andrzej Sapkowski"));
        save(new Book(null, "Rok 1984", "George Orwell"));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    @Override
    public List<Book> findByAuthorOrTitle(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lowerKeyword) || 
                             b.getAuthor().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    @Override
    public void save(Book book) {
        if (book.getId() == null) {
            book.setId(idCounter++);
            books.add(book);
        } else {
            findById(book.getId()).ifPresent(existingBook -> {
                existingBook.setTitle(book.getTitle());
                existingBook.setAuthor(book.getAuthor());
            });
        }
    }

    @Override
    public void deleteById(Long id) {
        books.removeIf(b -> b.getId().equals(id));
    }
}