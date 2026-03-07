package com.deadperhaps.library.repository;

import com.deadperhaps.library.domain.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();
    List<Book> findByAuthorOrTitle(String keyword);
    Optional<Book> findById(Long id);
    void save(Book book);
    void deleteById(Long id);
}