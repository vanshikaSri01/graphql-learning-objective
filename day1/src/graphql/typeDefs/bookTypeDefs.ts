import { gql } from "apollo-server-express";

const bookType = gql`
  type Book {
    id: ID!
    title: String!
    genre: String!
    author: Author
  }

  type Query {
    getAllBooks: [Book]
    getBook(id: ID!): Book
  }

  type Mutation {
    addBook(title: String!, genre: String!, authorId: ID!): Book
  }
`;

export default bookType;
