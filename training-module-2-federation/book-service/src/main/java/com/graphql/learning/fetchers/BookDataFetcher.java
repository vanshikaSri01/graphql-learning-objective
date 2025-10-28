package com.graphql.learning.fetchers;

import com.netflix.graphql.dgs.*;
import com.graphql.learning.data.Book;
import java.util.*;

@DgsComponent
public class BookDataFetcher {

    private static final List<Book> BOOKS = List.of(
        new Book("1", "The Hobbit", 1937, "1"),
        new Book("2", "1984", 1949, "2"),
        new Book("3", "Clean Code", 2008, "2")
    );

    @DgsQuery
    public List<Book> books() {
        return BOOKS;
    }

    @DgsQuery
    public Book book(@InputArgument String id) {
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
}
