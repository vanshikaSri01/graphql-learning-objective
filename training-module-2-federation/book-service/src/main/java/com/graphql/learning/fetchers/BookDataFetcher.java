package com.graphql.learning.fetchers;

import com.netflix.graphql.dgs.*;
import com.graphql.learning.data.Book;
import java.util.*;

@DgsComponent
public class BookDataFetcher {

    // ✅ Use mutable list for adding new books
    private static final List<Book> BOOKS = new ArrayList<>(List.of(
        new Book("Book1", "The Hobbit", 1937, "1"),
        new Book("Book2", "1984", 1949, "2"),
        new Book("Book3", "Clean Code", 2008, "3")
    ));

    @DgsQuery
    public List<Book> getAllBooks() {
        return BOOKS;
    }

    @DgsQuery
    public Book getBookById(@InputArgument String id) {
        return BOOKS.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @DgsEntityFetcher(name = "Book")
    public Book resolveBook(Map<String, Object> values) {
        String id = (String) values.get("id");
        return BOOKS.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // 🆕 Mutation to add a new Book
    @DgsMutation
    public Book addBook(@InputArgument String title,
                        @InputArgument Integer publishedYear,
                        @InputArgument String authorId) {

        // generate a new ID (simple UUID for demo)
        String newId = "Book"+ UUID.randomUUID().toString();

        Book newBook = new Book(newId, title, publishedYear, authorId);
        BOOKS.add(newBook);

        return newBook;
    }
}
