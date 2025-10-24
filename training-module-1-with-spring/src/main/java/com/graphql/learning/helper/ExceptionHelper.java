package com.graphql.learning.helper;

public class ExceptionHelper {
    public static RuntimeException throwResourceNotFoundException() {
         return new RuntimeException("Resource not found !!");
    }

    public static RuntimeException throwAuthorNotFoundException(){
        return new RuntimeException("Author not found with the provided author id");
    }
    
}
