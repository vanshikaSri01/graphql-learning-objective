package com.graphql.learning.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.graphql.learning.entities.AuthorEntity;
import com.graphql.learning.entities.BookEntity;
import com.graphql.learning.services.AuthorService;
import com.graphql.learning.services.BookService;

@Controller
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(BookService bookService, AuthorService authorService){
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @MutationMapping
    public BookEntity createBook(@Argument String title, @Argument String genre, @Argument int pages, @Argument Long authorId){
        BookEntity book = new BookEntity();
        AuthorEntity author = authorService.getAuthorById(authorId);

        book.setTitle(title);
        book.setGenre(genre);
        book.setPages(pages);
        book.setAuthor(author);

        return bookService.createBook(book);
    }

    @MutationMapping
    public Boolean deleteBook(@Argument Long id){
        return bookService.deleteBook(id);
    }

    @QueryMapping
    public List<BookEntity> getAllBooks(){
        return bookService.getAllBooks();
    }

    @QueryMapping
    public BookEntity getBookById(@Argument Long id){
        return bookService.getBookById(id);
    }
}
