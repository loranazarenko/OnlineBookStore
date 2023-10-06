package com.example.onlinebookstore.service;

import com.example.onlinebookstore.entity.Book;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
