package com.graphql.learning;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.graphql.learning.entities.AuthorEntity;
import com.graphql.learning.entities.BookEntity;
import com.graphql.learning.repositories.AuthorRepository;
import com.graphql.learning.repositories.BookRepository;

@SpringBootApplication
public class GraphqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoData(AuthorRepository authorRepo, BookRepository bookRepo) {
		return args -> {
			// Create authors
			AuthorEntity author1 = new AuthorEntity(1l,"J.K. Rowling", "United Kingdom",null);
			AuthorEntity author2 = new AuthorEntity(2l, "George R.R. Martin", "United States", null);
			AuthorEntity author3 = new AuthorEntity(3l, "Haruki Murakami", "Japan", null);

			authorRepo.save(author1);
			authorRepo.save(author2);
			authorRepo.save(author3);

			// Create books
			bookRepo.save(new BookEntity(1l, "Harry Potter and the Sorcerer's Stone", "Fantasy", 309, author1));
			bookRepo.save(new BookEntity(2l, "Harry Potter and the Chamber of Secrets", "Fantasy", 341, author1));
			bookRepo.save(new BookEntity(3l, "A Game of Thrones", "Fantasy", 694, author2));
			bookRepo.save(new BookEntity(4l, "Kafka on the Shore", "Fiction", 505, author3));
			bookRepo.save(new BookEntity(5l, "Norwegian Wood", "Romance", 296, author3));

			System.out.println("✅ Demo data initialized successfully!");
		};
	}
}
