# GraphQL Learning Objective – Day 1

## Overview

This project is a **GraphQL API** built with **Apollo Server** and **Express**, using **TypeScript**.  
It manages a simple in-memory database of **Authors** and **Books**, allowing queries and mutations with nested relationships.

---

## Features

### Queries
- `getAllAuthors` – Retrieve a list of all authors.
- `getAuthor(id: ID!)` – Retrieve a single author by ID.
- `getAllBooks` – Retrieve a list of all books.
- `getBook(id: ID!)` – Retrieve a single book by ID.

### Mutations
- `addAuthor(name: String!)` – Add a new author.
- `addBook(title: String!, genre: String!, authorId: ID!)` – Add a new book.

### Nested Resolvers
- Each `Book` contains its `author`.
- Each `Author` contains their `books`.

---

## Tech Stack
- Node.js
- TypeScript
- Express
- Apollo Server
- GraphQL

---

## Project Structure
```
day1/
├─ src/
│ ├─ index.ts              # Entry point
│ ├─ graphql/
│ │ ├─ schema.ts           # Combines typeDefs & resolvers
│ │ ├─ typeDefs/
│ │ │ ├─ authorTypeDefs.ts
│ │ │ └─ bookTypeDefs.ts
│ │ └─ resolvers/
│ │ ├─ authorResolver.ts
│ │ └─ bookResolver.ts
│ └─ data/
│ └─ db.ts                 # In-memory data for authors & books
├─ package.json
├─ tsconfig.json
└─ .gitignore
```
---

## Getting Started

### 1. Install dependencies

```
 npm install
```

### 2. Run the Server (Development)

To start the server in development mode, run:

```
npm start
```
- The server will run at:
http://localhost:4000/graphql
- We can use GraphQL Playground to test queries and mutations.

### 3. Example Queries
- Get all books with authors
```
query {
  getAllBooks {
    title
    author {
      name
    }
  }
}
```

- Add a new author
```
mutation {
  addAuthor(name: "Ernest Hemingway") {
    id
    name
  }
}
```
