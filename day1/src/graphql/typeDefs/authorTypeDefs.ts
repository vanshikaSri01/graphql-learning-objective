import { gql } from "apollo-server-express";

const authorType = gql`
  type Author {
    id: ID!
    name: String!
    books: [Book]
  }

  type Query {
    getAllAuthors: [Author]
    getAuthor(id: ID!): Author
  }

  type Mutation {
    addAuthor(name: String!): Author
  }
`;

export default authorType;
