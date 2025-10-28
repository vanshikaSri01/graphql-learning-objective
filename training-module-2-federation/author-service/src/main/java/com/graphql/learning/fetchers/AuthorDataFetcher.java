package com.graphql.learning.fetchers;

import com.graphql.learning.data.Author;
import com.graphql.learning.data.Book;
import com.netflix.graphql.dgs.*;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Map;

@DgsComponent
public class AuthorDataFetcher {

    private static final List<Author> AUTHORS = List.of(
        new Author("1", "J.R.R. Tolkien"),
        new Author("2", "George Orwell"),
        new Author("3", "Robert C. Martin")
    );

    @DgsEntityFetcher(name = "Book")
    public Book getBook(Map<String, Object> values) {
        String id = (String) values.get("id");
        return new Book(id); 
    }

    @DgsEntityFetcher(name = "Author")
    public Author author(Map<String, Object> values) {
        String id = (String) values.get("id");
        return AUTHORS.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @DgsData(parentType = "Book", field = "author")
    public Author getAuthor(DataFetchingEnvironment dfe) {
    Book book = dfe.getSource();  
    String bookId = book.getId();

    return AUTHORS.stream()
            .filter(a -> a.getId().equals(bookId))
            .findFirst()
            .orElse(null);
}

}
