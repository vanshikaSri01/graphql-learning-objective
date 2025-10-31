package com.graphql.book.resolver;

import com.graphql.book.directives.Auth;
import com.graphql.book.model.Book;
import com.netflix.graphql.dgs.*;

import java.util.*;

@DgsComponent
public class BookResolver {

    private static final List<Book> BOOKS = new ArrayList<>(List.of(
            new Book(1L, "GraphQL in Action", 2024, 1L),
            new Book(2L, "Spring Boot with DGS", 2025, 2L)
    ));

    @DgsQuery
    @Auth(requires = "USER")
    public List<Book> getAllBooks() {
        return BOOKS;
    }

    @DgsQuery
    @Auth(requires = "USER")
    public Book getBookById(@InputArgument Long id) {
        return BOOKS.stream().filter(b -> b.id().equals(id)).findFirst().orElse(null);
    }

    @DgsMutation
    @Auth(requires = "ADMIN")
    public Book addBook(@InputArgument String title, @InputArgument int publishedYear, @InputArgument Long authorId) {
        Book newBook = new Book((long) (BOOKS.size() + 1), title, publishedYear, authorId);
        BOOKS.add(newBook);
        return newBook;
    }

    @DgsMutation
    @Auth(requires = "ADMIN")
    public Boolean deleteBook(@InputArgument Long id) {
        return BOOKS.removeIf(b -> b.id().equals(id));
    }
}
