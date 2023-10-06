package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.entity.Book;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
