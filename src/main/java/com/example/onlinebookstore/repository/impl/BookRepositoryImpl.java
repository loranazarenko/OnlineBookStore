package com.example.onlinebookstore.repository.impl;

import com.example.onlinebookstore.entity.Book;
import com.example.onlinebookstore.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return null;
    }
}
