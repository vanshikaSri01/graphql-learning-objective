package com.graphql.learning.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.graphql.learning.entities.BookEntity;
import com.graphql.learning.helper.ExceptionHelper;
import com.graphql.learning.repositories.AuthorRepository;
import com.graphql.learning.repositories.BookRepository;

@Service
public class BookService {
    private final BookRepository bookRepo;

    public BookService(AuthorRepository authorRepo, BookRepository bookRepo){
        this.bookRepo = bookRepo;
    }

    public BookEntity createBook(BookEntity book){
        return bookRepo.save(book);
    }

    public List<BookEntity> getAllBooks(){
        return bookRepo.findAll();
    }

    public BookEntity getBookById(Long id){
        BookEntity book = bookRepo.findById(id).orElseThrow(ExceptionHelper::throwResourceNotFoundException);
        return book;
    }

    public boolean deleteBook(Long id){
        BookEntity book = bookRepo.findById(id).orElseThrow(ExceptionHelper::throwResourceNotFoundException);
        bookRepo.delete(book);
        return true;
    }
}
