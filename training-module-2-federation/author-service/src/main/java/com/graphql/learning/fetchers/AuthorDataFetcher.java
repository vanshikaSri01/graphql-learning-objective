package com.graphql.learning.fetchers;

import com.graphql.learning.data.Author;
import com.graphql.learning.data.Book;
import com.netflix.graphql.dgs.*;
import graphql.schema.DataFetchingEnvironment;

import java.util.*;

@DgsComponent
public class AuthorDataFetcher {

    // ✅ Mutable list so we can add authors
    private static final List<Author> AUTHORS = new ArrayList<>(List.of(
        new Author("1", "J.R.R. Tolkien"),
        new Author("2", "George Orwell"),
        new Author("3", "Robert C. Martin")
    ));

    @DgsEntityFetcher(name = "Book")
    public Book getBook(Map<String, Object> values) {
        String id = (String) values.get("id");
        String authorId = (String) values.get("authorId");
        return new Book(id, authorId);
}

    @DgsEntityFetcher(name = "Author")
    public Author resolveAuthor(Map<String, Object> values) {
        String id = (String) values.get("id");
        return AUTHORS.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @DgsData(parentType = "Book", field = "author")
    public Author getAuthor(DataFetchingEnvironment dfe) {
    Book book = dfe.getSource();
    String authorId = book.getAuthorId();

    return AUTHORS.stream()
            .filter(a -> a.getId().equals(authorId))
            .findFirst()
            .orElse(null);
}

    @DgsQuery
    public List<Author> getAllAuthors() {
        return AUTHORS;
    }

    @DgsQuery
    public Author getAuthorById(@InputArgument String id) {
        return AUTHORS.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // 🆕 Mutation: Add a new author
    @DgsMutation
    public Author addAuthor(@InputArgument String name) {
        String newId = UUID.randomUUID().toString();

        Author newAuthor = new Author(newId, name);
        AUTHORS.add(newAuthor);

        return newAuthor;
    }
}
