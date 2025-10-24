package com.graphql.learning.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.graphql.learning.entities.AuthorEntity;
import com.graphql.learning.helper.ExceptionHelper;
import com.graphql.learning.repositories.AuthorRepository;

@Service
public class AuthorService {
    private final AuthorRepository authorRepo;

    public AuthorService(AuthorRepository authorRepo){
        this.authorRepo = authorRepo;
    }

    public AuthorEntity createAuthor(AuthorEntity author){
        return authorRepo.save(author);
    }

    public List<AuthorEntity> getAllAuthors(){
        return authorRepo.findAll();
    }

    public AuthorEntity getAuthorById(Long id){
        AuthorEntity author = authorRepo.findById(id).orElseThrow(ExceptionHelper::throwResourceNotFoundException);
        return author;
    }

    public boolean deleteAuthor(Long id){
        AuthorEntity author = authorRepo.findById(id).orElseThrow(ExceptionHelper::throwResourceNotFoundException);
        authorRepo.delete(author);
        return true;
    }   

}
