package com.graphql.learning.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.graphql.learning.entities.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
}