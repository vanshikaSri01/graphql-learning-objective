package com.graphql.book.model;

public record Book(Long id, String title, int publishedYear, Long authorId) {}
