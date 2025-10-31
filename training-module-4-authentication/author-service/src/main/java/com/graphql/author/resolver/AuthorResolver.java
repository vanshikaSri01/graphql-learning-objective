package com.graphql.author.resolver;

import com.graphql.author.directives.Auth;
import com.graphql.author.model.Author;
import com.graphql.author.model.Book;
import com.netflix.graphql.dgs.*;

import graphql.schema.DataFetchingEnvironment;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DgsComponent
@Component
public class AuthorResolver {

    // Simple in-memory author list (simulating a database)
    private static final List<Author> authors = new ArrayList<>(List.of(
            new Author(1L, "John Doe"),
            new Author(2L, "Jane Smith")
    ));

    @DgsQuery
    @Auth(requires = "USER")
    public List<Author> getAllAuthors() {
        return authors;
    }

    @DgsQuery
    @Auth(requires = "USER")
    public Author getAuthorById(@InputArgument Long id) {
        return authors.stream()
                .filter(author -> author.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @DgsMutation
    @Auth(requires = "ADMIN")
    public Author addAuthor(@InputArgument String name) {
        long newId = authors.size() + 1L;
        Author newAuthor = new Author(newId, name);
        authors.add(newAuthor);
        return newAuthor;
    }

    @DgsMutation
    @Auth(requires = "ADMIN")
    public Boolean deleteAuthor(@InputArgument Long id) {
        return authors.removeIf(author -> author.id().equals(id));
    }
    

    // Apollo Federation entity fetcher (for resolving author in Book subgraph)
    @DgsEntityFetcher(name = "Author")
    public Author getAuthorByIdEntity(Map<String, Object> values) {
        Long id = Long.parseLong((String) values.get("id"));
        return authors.stream()
                .filter(author -> author.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @DgsEntityFetcher(name = "Book")
    public Book getBookEntity(Map<String, Object> values) {
        Long id = Long.parseLong(values.get("id").toString());
        Long authorId = values.containsKey("authorId") ? Long.parseLong(values.get("authorId").toString()) : null;
        return new Book(id, authorId);
    }

    @DgsData(parentType = "Book", field = "author")
    public Author getAuthor(DataFetchingEnvironment dfe) {
    Book book = dfe.getSource();
    Long authorId = book.authorId();

    return authors.stream()
            .filter(a -> a.id().equals(authorId))
            .findFirst()
            .orElse(null);
    }
}
