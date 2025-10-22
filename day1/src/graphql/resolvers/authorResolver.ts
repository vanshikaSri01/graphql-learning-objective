import { authors, books } from "../../data/db.js";
import { IResolvers } from "@graphql-tools/utils";

interface Author {
  id: string;
  name: string;
}

interface Book {
  id: string;
  title: string;
  authorId: string;
}

const authorResolver: IResolvers = {
  Query: {
    getAllAuthors: () => authors,
    getAuthor: (_parent, { id }: { id: string }) =>
      authors.find((author: Author) => author.id === id),
  },
  Mutation: {
    addAuthor: (_parent, { name }: { name: string }) => {
      const newAuthor: Author = { id: String(authors.length + 1), name };
      authors.push(newAuthor);
      return newAuthor;
    },
  },
  Author: {
    books: (author: Author) => books.filter((book: Book) => book.authorId === author.id),
  },
};

export default authorResolver;
