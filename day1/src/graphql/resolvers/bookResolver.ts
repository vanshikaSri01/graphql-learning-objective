import { authors, books } from "../../data/db.js";
import { IResolvers } from "@graphql-tools/utils";

interface Author {
  id: string;
  name: string;
}

interface Book {
  id: string;
  title: string;
  genre: string;
  authorId: string;
}

const bookResolver: IResolvers = {
  Query: {
    getAllBooks: () => books,
    getBook: (_parent, { id }: { id: string }) => books.find((book: Book) => book.id === id),
  },
  Mutation: {
    addBook: (
      _parent,
      { title, genre, authorId }: { title: string; genre: string; authorId: string }
    ) => {
      const newBook: Book = { id: String(books.length + 1), title, genre, authorId };
      books.push(newBook);
      return newBook;
    },
  },
  Book: {
    author: (book: Book) => authors.find((author: Author) => author.id === book.authorId),
  },
};

export default bookResolver;
