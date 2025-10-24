package com.graphql.learning.controllers;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.graphql.learning.entities.AuthorEntity;
import com.graphql.learning.services.AuthorService;

@Controller
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    @MutationMapping
    public AuthorEntity createAuthor(@Argument String name, @Argument String country){
        AuthorEntity author = new AuthorEntity();
        author.setName(name);
        author.setCountry(country);
        return authorService.createAuthor(author);
    }

    @QueryMapping
    public List<AuthorEntity> getAllAuthors(){
        return authorService.getAllAuthors();
    }

    @QueryMapping
    public AuthorEntity getAuthorById(@Argument Long id){
        return authorService.getAuthorById(id);
    }

    @MutationMapping
    public Boolean deleteAuthor(@Argument Long id){
        return authorService.deleteAuthor(id);
    }
    
}
